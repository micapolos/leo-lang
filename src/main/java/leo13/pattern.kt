package leo13

import leo.base.Empty
import leo.base.empty
import leo.base.fold

sealed class Pattern
data class EmptyPattern(val empty: Empty) : Pattern()
data class LinkPattern(val link: PatternLink) : Pattern()

data class PatternLink(val lhs: Pattern, val choice: PatternChoice)

sealed class PatternChoice
data class EmptyPatternChoice(val empty: Empty) : PatternChoice()
data class LinkPatternChoice(val link: ChoiceLink) : PatternChoice()

data class ChoiceLink(val lhs: PatternChoice, val case: PatternCase)
data class PatternCase(val name: String, val rhs: Pattern)

val Pattern.isEmpty get() = (this is EmptyPattern)
val PatternChoice.isEmpty get() = (this is EmptyPatternChoice)

// --- constructors

fun pattern(empty: Empty): Pattern = EmptyPattern(empty)
fun pattern(link: PatternLink): Pattern = LinkPattern(link)

fun patternChoice(empty: Empty): PatternChoice = EmptyPatternChoice(empty)
fun choice(link: ChoiceLink): PatternChoice = LinkPatternChoice(link)

fun link(lhs: Pattern, choice: PatternChoice) = PatternLink(lhs, choice)
fun link(lhs: PatternChoice, line: PatternCase) = ChoiceLink(lhs, line)
infix fun String.caseTo(rhs: Pattern) = PatternCase(this, rhs)

fun Pattern.plus(choice: PatternChoice): Pattern = pattern(link(this, choice))
fun pattern(vararg choices: PatternChoice) = pattern(empty).fold(choices) { plus(it) }

fun PatternChoice.plus(case: PatternCase) = choice(link(this, case))
fun choice(vararg cases: PatternCase) = patternChoice(empty).fold(cases) { plus(it) }

// --- parse

val Script.pattern
	get() =
		when (this) {
			is EmptyScript -> pattern(empty)
			is LinkScript -> pattern(link.patternLink)
		}

val ScriptLink.patternLink: PatternLink
	get() =
		eitherPatternLinkOrNull ?: exactPatternLink

val ScriptLink.eitherPatternLinkOrNull: PatternLink?
	get() =
		eitherChoiceLinkOrNull?.let { choiceLink ->
			link(pattern(empty), choice(choiceLink))
		}

val Script.eitherChoiceOrNull: PatternChoice?
	get() =
		when (this) {
			is EmptyScript -> patternChoice(empty)
			is LinkScript -> link.eitherChoiceLinkOrNull?.let(::choice)
		}

val ScriptLink.eitherChoiceLinkOrNull: ChoiceLink?
	get() =
		lhs.eitherChoiceOrNull?.let { choice ->
			line.eitherCaseOrNull?.let { case ->
				link(choice, case)
			}
		}

val ScriptLine.eitherCaseOrNull: PatternCase?
	get() =
		if (name == "either" && rhs is LinkScript && rhs.link.lhs.isEmpty) rhs.link.line.patternCase
		else null

val ScriptLink.exactPatternLink: PatternLink
	get() =
		link(lhs.pattern, line.patternChoice)

val ScriptLine.patternChoice
	get() =
		choice(patternCase)

val ScriptLine.patternCase: PatternCase
	get() =
		name caseTo rhs.pattern

// --- isConstant

val Pattern.isConstant: Boolean
	get() =
		when (this) {
			is EmptyPattern -> true
			is LinkPattern -> link.isConstant
		}

val PatternLink.isConstant: Boolean
	get() =
		lhs.isConstant && choice.isConstant

val PatternChoice.isConstant: Boolean
	get() =
		when (this) {
			is EmptyPatternChoice -> true
			is LinkPatternChoice -> link.lhs.isEmpty && link.case.isConstant
		}

val PatternCase.isConstant
	get() =
		rhs.isConstant
