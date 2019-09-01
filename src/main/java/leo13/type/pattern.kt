package leo13.type

import leo.base.Empty
import leo.base.empty
import leo.base.failIfOr
import leo.base.fold
import leo13.LeoObject
import leo13.fail
import leo13.script.*

val patternName = "pattern"
val emptyName = "empty"

sealed class Pattern : LeoObject() {
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

// --- resolve recursion

fun Type.resolve(pattern: Pattern): Pattern =
	resolve(pattern, recursion)

fun Type.resolve(pattern: Pattern, recursion: Recursion): Pattern =
	when (pattern) {
		is EmptyPattern -> pattern
		is LinkPattern -> pattern(resolve(pattern.link, recursion))
		is ChoicePattern -> pattern(resolve(pattern.choice, recursion))
		is ArrowPattern -> pattern
	}

fun Type.resolve(patternLink: PatternLink, recursion: Recursion): PatternLink =
	link(resolve(patternLink.lhs, recursion), resolve(patternLink.line, recursion))

fun Type.resolve(patternLine: PatternLine, recursion: Recursion): PatternLine =
	patternLine.name lineTo resolve(patternLine.rhs, recursion)

fun Type.resolve(choice: Choice, recursion: Recursion): Choice =
	uncheckedChoice(
		resolve(choice.lhsNode, recursion),
		resolve(choice.case, recursion))

fun Type.resolve(choiceNode: ChoiceNode, recursion: Recursion): ChoiceNode =
	when (choiceNode) {
		is CaseChoiceNode -> choiceNode(resolve(choiceNode.case, recursion))
		is ChoiceChoiceNode -> node(resolve(choiceNode.choice, recursion))
	}

fun Type.resolve(case: Case, recursion: Recursion): Case =
	case.name caseTo resolve(case.rhs, recursion)

fun Type.resolve(patternRhs: PatternRhs, recursion: Recursion): PatternRhs =
	when (patternRhs) {
		is PatternPatternRhs -> rhs(resolve(patternRhs.pattern, recursion.recursion))
		is RecursionPatternRhs -> patternOrNull(recursion)?.let { rhs(it) }
			?: fail(
				scriptableBody
					.plus("resolve" lineTo script(
						patternRhs.scriptableLine,
						recursion.scriptableLine)))
	}
