package leo13.type.pattern

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo13.script.Script
import leo13.script.Scriptable
import leo13.script.lineTo
import leo13.script.script

sealed class Pattern : Scriptable() {
	override val scriptableName get() = "pattern"
	abstract val patternScriptableName: String
	abstract val patternScriptableBody: Script
}

data class EmptyPattern(val empty: Empty) : Pattern() {
	override fun toString() = super.toString()
	override val scriptableBody get() = script()
	override val patternScriptableName get() = "empty"
	override val patternScriptableBody get() = script()
}

data class LinkPattern(val link: PatternLink) : Pattern() {
	override fun toString() = super.toString()
	override val scriptableBody get() = patternScriptableBody
	override val patternScriptableName get() = link.scriptableName
	override val patternScriptableBody get() = link.scriptableBody
}

data class ChoicePattern(val choice: Choice) : Pattern() {
	override fun toString() = super.toString()
	override val scriptableBody get() = script(patternScriptableName lineTo patternScriptableBody)
	override val patternScriptableName get() = choice.scriptableName
	override val patternScriptableBody get() = choice.scriptableBody
}

data class ArrowPattern(val arrow: PatternArrow) : Pattern() {
	override fun toString() = super.toString()
	override val scriptableBody get() = script(patternScriptableName lineTo patternScriptableBody)
	override val patternScriptableName get() = arrow.scriptableName
	override val patternScriptableBody get() = arrow.scriptableBody
}

fun pattern(empty: Empty): Pattern = EmptyPattern(empty)
fun pattern(choice: Choice): Pattern = ChoicePattern(choice)
fun pattern(arrow: PatternArrow): Pattern = ArrowPattern(arrow)
fun pattern(link: PatternLink): Pattern = LinkPattern(link)

fun Pattern.plus(vararg lines: PatternLine) = fold(lines) { pattern(link(this, it)) }
fun pattern(vararg lines: PatternLine) = pattern(empty).plus(*lines)
fun pattern(name: String) = pattern(name lineTo pattern())

fun Pattern.contains(pattern: Pattern): Boolean =
	when (this) {
		is EmptyPattern -> pattern is EmptyPattern
		is ChoicePattern -> choice.contains(pattern)
		is ArrowPattern -> pattern is ArrowPattern && arrow.contains(pattern.arrow)
		is LinkPattern -> pattern is LinkPattern && link.contains(pattern.link) // TODO line should contain single case
	}
