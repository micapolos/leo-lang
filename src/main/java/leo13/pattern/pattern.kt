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
				is LinkPattern -> link.scriptingLine.rhs
				is OptionsPattern -> script(options.scriptingLine)
				is ArrowPattern -> arrow.scriptingLine.rhs
			}

	val isEmpty get() = this is EmptyPattern
	val linkOrNull get() = (this as? LinkPattern)?.link
	val optionsOrNull get() = (this as? OptionsPattern)?.options
	val arrowOrNull get() = (this as? ArrowPattern)?.arrow

	fun plus(item: PatternItem) =
		pattern(this linkTo item)

	fun plus(line: PatternLine) =
		plus(item(line))

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
			linkOrNull?.item?.line?.rhs

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

	fun expand(rootOrNull: RecurseRoot? = null): Pattern =
		when (this) {
			is EmptyPattern -> this
			is LinkPattern -> pattern(link.expand(rootOrNull))
			is OptionsPattern -> pattern(options.expand(rootOrNull))
			is ArrowPattern -> this
		}

	fun contains(pattern: Pattern, trace: PatternTrace? = null): Boolean =
		when (this) {
			is EmptyPattern -> pattern is EmptyPattern
			is LinkPattern -> pattern is LinkPattern && link.contains(pattern.link, trace)
			is OptionsPattern -> options.contains(pattern, trace)
			is ArrowPattern -> pattern is ArrowPattern && arrow.contains(pattern.arrow)
		}
}

data class EmptyPattern(val empty: Empty) : Pattern() {
	override fun toString() = super.toString()
}

data class LinkPattern(val link: PatternLink) : Pattern() {
	override fun toString() = super.toString()
}

data class OptionsPattern(val options: Options) : Pattern() {
	override fun toString() = super.toString()
}

data class ArrowPattern(val arrow: PatternArrow) : Pattern() {
	override fun toString() = super.toString()
}

fun pattern() = pattern(empty)
fun pattern(empty: Empty): Pattern = EmptyPattern(empty)
fun pattern(link: PatternLink): Pattern = LinkPattern(link)
fun pattern(options: Options): Pattern = OptionsPattern(options)
fun pattern(arrow: PatternArrow): Pattern = ArrowPattern(arrow)

fun pattern(name: String) = pattern(name.patternLine)
fun pattern(recurse: Recurse) = pattern(item(recurse))

fun pattern(item: PatternItem, vararg items: PatternItem) =
	pattern(pattern() linkTo item).fold(items) { plus(it) }

fun pattern(line: PatternLine, vararg lines: PatternLine) =
	pattern(pattern() linkTo line).fold(lines) { plus(it) }

fun pattern(name: String, vararg names: String) =
	pattern(pattern() linkTo name.patternLine).fold(names) { plus(it.patternLine) }

val Script.pattern
	get() =
		pattern().fold(lineStack.reverse) { plus(it.patternLine) }
