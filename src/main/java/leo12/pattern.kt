package leo12

import leo.base.Empty
import leo.base.empty
import leo.base.notNullIf
import leo10.Dict
import leo10.at
import leo10.dict
import leo10.set
import leo9.fold
import leo9.isEmpty
import leo9.mapOrNull
import leo9.reverse

sealed class Pattern
data class EmptyPattern(val empty: Empty) : Pattern()
data class BodyPattern(val body: PatternBody) : Pattern()

sealed class PatternBody
data class ExactPatternBody(val exact: PatternExact) : PatternBody()
data class ChoicePatternBody(val choice: PatternChoice) : PatternBody()

sealed class PatternExact
data class NamePatternExact(val name: String) : PatternExact()
data class LinkPatternExact(val link: PatternLink) : PatternExact()

data class PatternLink(val lhs: Pattern, val line: PatternLine)
data class PatternLine(val name: String, val rhs: PatternBody)

data class PatternChoice(val nameToOptionalPatternDict: Dict<Pattern>)

// --- constructors

fun pattern(empty: Empty): Pattern = EmptyPattern(empty)
fun pattern(body: PatternBody): Pattern = BodyPattern(body)
fun body(exact: PatternExact): PatternBody = ExactPatternBody(exact)
fun patternChoice(dict: Dict<Pattern>) = PatternChoice(dict)
fun body(choice: PatternChoice): PatternBody = ChoicePatternBody(choice)
fun patternExact(name: String): PatternExact = NamePatternExact(name)
fun exact(link: PatternLink): PatternExact = LinkPatternExact(link)
fun link(lhs: Pattern, line: PatternLine) = PatternLink(lhs, line)
infix fun String.lineTo(rhs: PatternBody) = PatternLine(this, rhs)

// --- parsing

val Script.pattern: Pattern
	get() = when (this) {
		is EmptyScript -> pattern(empty)
		is BodyScript -> pattern(body.patternBody)
	}

val ScriptBody.patternBody
	get() = when (this) {
		is NameScriptBody -> body(patternExact(name))
		is LinkScriptBody -> link.patternBody
	}

val ScriptLink.patternBody
	get() =
		choicePatternBodyOrNull ?: exactPatternBody

val ScriptLink.choicePatternBodyOrNull: PatternBody?
	get() =
		bodyStackOrNull("either")
			?.mapOrNull { headerOrNull }
			?.let { headerStack ->
				notNullIf(!headerStack.isEmpty) {
					body(patternChoice(dict<Pattern>(empty).fold(headerStack.reverse) {
						set(it.name, it.rhs.pattern)
					}))
				}
			}

val ScriptLink.exactPatternBody
	get() =
		body(exact(patternLink))

val ScriptLink.patternLink
	get() =
		link(lhs.pattern, line.patternLine)

val ScriptLine.patternLine: PatternLine
	get() =
		name lineTo rhs.patternBody

// --- matching

fun Pattern.matches(script: Script): Boolean =
	when (this) {
		is EmptyPattern -> script is EmptyScript
		is BodyPattern -> (script is BodyScript) && body.matches(script.body)
	}

fun PatternBody.matches(scriptBody: ScriptBody): Boolean =
	when (this) {
		is ExactPatternBody -> exact.matches(scriptBody)
		is ChoicePatternBody -> choice.matches(scriptBody)
	}

fun PatternExact.matches(scriptBody: ScriptBody) =
	when (this) {
		is NamePatternExact -> (scriptBody is NameScriptBody) && name == scriptBody.name
		is LinkPatternExact -> (scriptBody is LinkScriptBody) && link.matches(scriptBody.link)
	}

fun PatternChoice.matches(scriptBody: ScriptBody) =
	scriptBody.headerOrNull?.let { header ->
		nameToOptionalPatternDict.at(header.name)?.matches(header.rhs)
	} ?: false

fun PatternLink.matches(link: ScriptLink) =
	lhs.matches(link.lhs) && line.matches(link.line)

fun PatternLine.matches(line: ScriptLine) =
	name == line.name && rhs.matches(line.rhs)