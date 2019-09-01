package leo13.type

import leo.base.Empty
import leo.base.empty
import leo.base.failIfOr
import leo.base.fold
import leo13.Scriptable
import leo13.script.*

val patternName = "pattern"
val emptyName = "empty"

sealed class Pattern : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = patternName
	override val scriptableBody get() = typeScriptableBody
	abstract val typeScriptableName: String
	abstract val typeScriptableBody: Script
}

data class EmptyPattern(val empty: Empty) : Pattern() {
	override fun toString() = super.toString()
	override val typeScriptableName get() = emptyName
	override val typeScriptableBody get() = script()
}

data class LinkPattern(val link: PatternLink) : Pattern() {
	override fun toString() = super.toString()
	override val typeScriptableName get() = link.scriptableName
	override val typeScriptableBody get() = link.scriptableBody
}

data class ChoicePattern(val choice: Choice) : Pattern() {
	override fun toString() = super.toString()
	override val typeScriptableName get() = choice.scriptableName
	override val typeScriptableBody get() = choice.scriptableBody
}

data class ArrowPattern(val arrow: TypeArrow) : Pattern() {
	override fun toString() = super.toString()
	override val typeScriptableName get() = arrow.scriptableName
	override val typeScriptableBody get() = arrow.scriptableBody
}

// --- constructors

fun pattern(empty: Empty): Pattern = EmptyPattern(empty)
fun pattern(link: PatternLink): Pattern = LinkPattern(link)
fun pattern(choice: Choice): Pattern = ChoicePattern(choice)
fun pattern(arrow: TypeArrow): Pattern = ArrowPattern(arrow)

fun pattern(vararg lines: PatternLine): Pattern = pattern(empty).fold(lines) { plus(it) }
fun pattern(name: String): Pattern = pattern(name lineTo pattern())

fun Pattern.plus(line: PatternLine): Pattern = pattern(link(this, line))

val Pattern.isEmpty: Boolean get() = this is EmptyPattern
val Pattern.linkOrNull: PatternLink? get() = (this as? LinkPattern)?.link
val Pattern.onlyLineOrNull: PatternLine? get() = linkOrNull?.onlyLineOrNull

val Script.pattern: Pattern
	get() =
		pattern().fold(lineSeq) { plus(it.patternLine) }

val ScriptLine.patternLine: PatternLine
	get() =
		name lineTo rhs.pattern

fun Pattern.contains(pattern: Pattern): Boolean =
	when (this) {
		is EmptyPattern -> pattern is EmptyPattern
		is ChoicePattern -> choice.contains(pattern)
		is ArrowPattern -> pattern is ArrowPattern && arrow == pattern.arrow
		is LinkPattern -> pattern is LinkPattern && link.contains(pattern.link)
	}

val ScriptLine.unsafePattern: Pattern
	get() =
		failIfOr(name != patternName) {
			rhs.unsafePattern
		}

val Script.unsafePattern: Pattern
	get() =
		pattern().fold(lineSeq) { unsafePlus(it) }

fun Pattern.unsafePlus(scriptLine: ScriptLine): Pattern =
	when (this) {
		is EmptyPattern -> plus(scriptLine.unsafePatternLine)
		is LinkPattern -> link.unsafePlusType(scriptLine)
		is ChoicePattern -> choice.unsafePlusType(scriptLine)
		is ArrowPattern -> arrow.unsafePlusType(scriptLine)
	}

fun PatternLink.unsafePlusType(scriptLine: ScriptLine): Pattern =
	when {
		scriptLine.name == "or" && lhs is EmptyPattern -> pattern(uncheckedChoice(choiceNode(line.case), scriptLine.rhs.unsafeCase))
		scriptLine.name == "to" -> pattern(arrow(type(pattern(this)), type(scriptLine.rhs.unsafePattern)))
		else -> pattern(plus(scriptLine.unsafePatternLine))
	}

fun Choice.unsafePlusType(scriptLine: ScriptLine): Pattern =
	if (scriptLine.name == "or") pattern(unsafePlus(scriptLine.rhs.unsafeCase))
	else pattern(this).plus(scriptLine.unsafePatternLine)

fun TypeArrow.unsafePlusType(scriptLine: ScriptLine): Pattern =
	if (scriptLine.name == "to") pattern(arrow(type(pattern(this)), type(scriptLine.rhs.unsafePattern)))
	else pattern(this).plus(scriptLine.unsafePatternLine)

val Pattern.previousOrNull: Pattern?
	get() = when (this) {
		is EmptyPattern -> null
		is LinkPattern -> link.lhs
		is ChoicePattern -> pattern()
		is ArrowPattern -> pattern()
	}

val Pattern.lineOrNull: Pattern?
	get() = when (this) {
		is EmptyPattern -> null
		is LinkPattern -> pattern(link.line)
		is ChoicePattern -> this
		is ArrowPattern -> this
	}

val Pattern.rhsThunkOrNull: PatternRhs?
	get() = linkOrNull?.line?.rhs

// === type to script

val Pattern.unsafeStaticScript: Script
	get() = when (this) {
		is EmptyPattern -> script()
		is LinkPattern -> link.unsafeStaticScriptLink.script
		is ChoicePattern -> error("non-static")
		is ArrowPattern -> error("non-static")
	}

val PatternLine.unsafeStaticScriptLine: ScriptLine
	get() =
		name lineTo rhs.unsafeStaticScript

val PatternLink.unsafeStaticScriptLink: ScriptLink
	get() =
		link(lhs.unsafeStaticScript, line.unsafeStaticScriptLine)

// --- rhsOrNull

fun Pattern.rhsOrNull(name: String): Pattern? =
	type.rhsOrNull(name)?.pattern

fun Pattern.rhsThunkOrNull(name: String): PatternRhs? =
	when (this) {
		is EmptyPattern -> null
		is LinkPattern -> link.rhsThunkOrNull(name)
		is ChoicePattern -> null
		is ArrowPattern -> null
	}

fun PatternLink.rhsThunkOrNull(name: String): PatternRhs? =
	if (line.name == name) line.rhs
	else lhs.rhsThunkOrNull(name)

// --- accessOrNull

fun Pattern.accessOrNull(name: String): Pattern? =
	type.accessOrNull(name)?.pattern

val Pattern.unsafeArrow
	get() =
		if (this is ArrowPattern) arrow
		else error("not arrow $this")
