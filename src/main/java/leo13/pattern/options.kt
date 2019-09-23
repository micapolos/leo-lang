package leo13.pattern

import leo.base.fold
import leo13.Empty
import leo13.ObjectScripting
import leo13.empty
import leo13.optionsName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

sealed class Options : ObjectScripting() {
	override fun toString() = scriptingLine.toString()

	override val scriptingLine: ScriptLine
		get() =
			optionsName lineTo when (this) {
				is EmptyOptions -> script()
				is LinkOptions -> link.scriptingLine.rhs
			}

	fun recurseExpand(rootRecurse: Recurse?, rootNode: PatternNode): Options =
		when (this) {
			is EmptyOptions -> this
			is LinkOptions -> options(link.recurseExpand(rootRecurse, rootNode))
		}

	fun recurseContains(options: Options, trace: PatternTrace): Boolean =
		when (this) {
			is EmptyOptions -> options is EmptyOptions
			is LinkOptions -> options is LinkOptions && link.recurseContains(options.link, trace)
		}

	fun recurseContains(line: PatternLine, trace: PatternTrace): Boolean =
		when (this) {
			is EmptyOptions -> false
			is LinkOptions -> link.recurseContains(line, trace)
		}

	fun recurseContains(link: PatternLink, trace: PatternTrace): Boolean =
		link.lhs.isEmpty && recurseContains(link.line, trace)

	fun recurseContains(node: PatternNode, trace: PatternTrace): Boolean =
		when (node) {
			is EmptyPatternNode -> false
			is LinkPatternNode -> recurseContains(node.link, trace)
			is OptionsPatternNode -> recurseContains(node.options, trace)
			is ArrowPatternNode -> false
		}
}

data class EmptyOptions(val empty: Empty) : Options() {
	override fun toString() = super.toString()
}

data class LinkOptions(val link: OptionsLink) : Options() {
	override fun toString() = super.toString()
}

fun options(empty: Empty): Options = EmptyOptions(empty)
fun options(link: OptionsLink): Options = LinkOptions(link)

// TODO: Rename to plusOrNull and detect duplicates
fun Options.plus(line: PatternLine) = options(linkTo(line))

fun options(vararg lines: PatternLine) = options(empty).fold(lines) { plus(it) }
fun options(name: String, vararg names: String) = options(name lineTo pattern()).fold(names) { plus(it lineTo pattern()) }

fun Options.contains(options: Options): Boolean =
	when (this) {
		is EmptyOptions -> options is EmptyOptions
		is LinkOptions -> options is LinkOptions && link.contains(options.link)
	}

fun Options.contains(pattern: Pattern): Boolean =
	pattern is NodePattern && contains(pattern.node)

fun Options.contains(pattern: PatternNode): Boolean =
	when (pattern) {
		is LinkPatternNode -> contains(pattern.link)
		is OptionsPatternNode -> contains(pattern.options)
		else -> false
	}

fun Options.contains(patternLink: PatternLink): Boolean =
	when (this) {
		is EmptyOptions -> false
		is LinkOptions -> link.contains(patternLink)
	}

fun Options.contains(line: PatternLine) =
	when (this) {
		is EmptyOptions -> false
		is LinkOptions -> link.contains(line)
	}

tailrec fun Options.plusReversed(options: Options): Options =
	when (options) {
		is EmptyOptions -> this
		is LinkOptions -> plus(options.link.line).plusReversed(options.link.lhs)
	}