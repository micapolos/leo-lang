package leo13.pattern

import leo.base.fold
import leo13.*
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

sealed class Pattern : ObjectScripting() {
	override fun toString() = scriptingLine.toString()

	override val scriptingLine: ScriptLine
		get() = patternName lineTo
			when (this) {
				is EmptyPattern -> empty.scriptingLine.rhs
				is OptionsPattern -> script(options.scriptingLine)
				is ArrowPattern -> arrow.scriptingLine.rhs
				is FunctionPattern -> function.scriptingLine.rhs
				is GivenPattern -> given.scriptLine.rhs
				is ApplyPattern -> apply.scriptingLine.rhs
				is LinkPattern -> link.scriptingLine.rhs
			}

	val isEmpty get() = this is EmptyPattern
	val optionsOrNull get() = (this as? OptionsPattern)?.options
	val arrowOrNull get() = (this as? ArrowPattern)?.arrow
	val functionOrNull get() = (this as? FunctionPattern)?.function
	val givenOrNull get() = (this as? GivenPattern)?.given
	val applyOrNull get() = (this as? ApplyPattern)?.apply
	val linkOrNull get() = (this as? LinkPattern)?.link
	val lineOrNull get() = linkOrNull?.line

	fun plus(vararg lines: PatternLine): Pattern =
		fold(lines) { pattern(linkTo(it)) }

	fun contains(pattern: Pattern, context: PatternContext = patternContext()): Boolean =
		when (this) {
			is EmptyPattern -> pattern.isEmpty
			is OptionsPattern -> options.contains(pattern)
			is ArrowPattern -> pattern is ArrowPattern && arrow.contains(pattern.arrow)
			is FunctionPattern -> false // TODO: Or maybe we can do something about it?
			is GivenPattern -> TODO()
			is ApplyPattern -> TODO()
			is LinkPattern -> pattern is LinkPattern && link.contains(pattern.link)
		}

	fun lineRhsOrNull(name: String): Pattern? =
		linkOrNull?.lineRhsOrNull(name)

	fun setLineRhsOrNull(line: PatternLine): Pattern? =
		linkOrNull?.setLineRhsOrNull(line)?.let { pattern(it) }

	fun getOrNull(name: String): Pattern? =
		linkOrNull?.getOrNull(name)

	fun setOrNull(line: PatternLine): Pattern? =
		linkOrNull?.setOrNull(line)?.let { pattern(it) }

	val previousOrNull: Pattern?
		get() =
			linkOrNull?.lhs

	val contentOrNull: Pattern?
		get() =
			linkOrNull?.line?.rhs

	val onlyNameOrNull: String?
		get() =
			linkOrNull?.onlyLineOrNull?.onlyNameOrNull

	fun leafPlusOrNull(pattern: Pattern): Pattern? =
		when (this) {
			is EmptyPattern -> pattern
			is LinkPattern -> link.leafPlusOrNull(pattern)?.let { pattern(it) }
			else -> null
		}

	val beginOptionsOrNull: Options?
		get() =
			when (this) {
				is EmptyPattern -> options()
				is OptionsPattern -> options
				else -> null
			}
}

data class EmptyPattern(val empty: Empty) : Pattern()
data class OptionsPattern(val options: Options) : Pattern()
data class ArrowPattern(val arrow: PatternArrow) : Pattern()
data class FunctionPattern(val function: PatternFunction) : Pattern()
data class GivenPattern(val given: Given) : Pattern()
data class ApplyPattern(val apply: PatternApply) : Pattern()
data class LinkPattern(val link: PatternLink) : Pattern()

fun pattern() = pattern(empty)
fun pattern(empty: Empty): Pattern = EmptyPattern(empty)
fun pattern(options: Options): Pattern = OptionsPattern(options)
fun pattern(arrow: PatternArrow): Pattern = ArrowPattern(arrow)
fun pattern(function: PatternFunction): Pattern = FunctionPattern(function)
fun pattern(given: Given): Pattern = GivenPattern(given)
fun pattern(apply: PatternApply): Pattern = ApplyPattern(apply)
fun pattern(link: PatternLink): Pattern = LinkPattern(link)

fun pattern(line: PatternLine, vararg lines: PatternLine) =
	pattern(pattern() linkTo line).plus(*lines)

fun pattern(name: String, vararg names: String) =
	pattern(name lineTo pattern()).fold(names) { plus(name lineTo pattern()) }

val Script.pattern
	get() =
		pattern().fold(lineStack.reverse) { plus(it.patternLine) }
