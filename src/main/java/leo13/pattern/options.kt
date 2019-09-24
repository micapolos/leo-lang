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

	fun expand(rootOrNull: RecurseRoot?): Options =
		when (this) {
			is EmptyOptions -> this
			is LinkOptions -> options(link.expand(rootOrNull))
		}

	fun contains(options: Options, trace: PatternTrace? = null): Boolean =
		when (this) {
			is EmptyOptions -> options is EmptyOptions
			is LinkOptions -> options is LinkOptions && link.contains(options.link, trace)
		}

	fun contains(item: PatternItem, traceOrNull: PatternTrace? = null): Boolean =
		contains(item.line, traceOrNull)

	fun contains(line: PatternLine, trace: PatternTrace? = null): Boolean =
		when (this) {
			is EmptyOptions -> false
			is LinkOptions -> link.contains(line, trace)
		}

	fun contains(link: PatternLink, trace: PatternTrace? = null): Boolean =
		link.lhs.isEmpty && contains(link.item, trace)

	fun contains(node: Pattern, trace: PatternTrace? = null): Boolean =
		when (node) {
			is EmptyPattern -> false
			is LinkPattern -> contains(node.link, trace)
			is OptionsPattern -> contains(node.options, trace)
			is ArrowPattern -> false
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
fun Options.plus(item: PatternItem) = options(linkTo(item))
fun Options.plus(line: PatternLine) = options(linkTo(line))

fun options(vararg lines: PatternLine) = options(empty).fold(lines) { plus(it) }
fun options(name: String, vararg names: String) = options(name lineTo pattern()).fold(names) { plus(it lineTo pattern()) }

tailrec fun Options.plusReversed(options: Options): Options =
	when (options) {
		is EmptyOptions -> this
		is LinkOptions -> plus(options.link.item).plusReversed(options.link.lhs)
	}