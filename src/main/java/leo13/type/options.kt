package leo13.type

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

	// TODO: Rename to plusOrNull and detect duplicates
	fun plus(item: TypeItem) = options(linkTo(item))

	fun plus(line: TypeLine) = plus(item(line))
	fun plus(name: String) = plus(name lineTo type())

	fun expand(rootOrNull: RecurseRoot?): Options =
		when (this) {
			is EmptyOptions -> this
			is LinkOptions -> options(link.expand(rootOrNull))
		}

	fun contains(options: Options, trace: TypeTrace? = null): Boolean =
		when (this) {
			is EmptyOptions -> options is EmptyOptions
			is LinkOptions -> options is LinkOptions && link.contains(options.link, trace)
		}

	fun contains(item: TypeItem, traceOrNull: TypeTrace? = null): Boolean =
		contains(item.line, traceOrNull)

	fun contains(line: TypeLine, trace: TypeTrace? = null): Boolean =
		when (this) {
			is EmptyOptions -> false
			is LinkOptions -> link.contains(line, trace)
		}

	fun contains(link: TypeLink, trace: TypeTrace? = null): Boolean =
		link.lhs.isEmpty && contains(link.item, trace)

	fun contains(node: Type, trace: TypeTrace? = null): Boolean =
		when (node) {
			is EmptyType -> false
			is LinkType -> contains(node.link, trace)
			is OptionsType -> contains(node.options, trace)
			is ArrowType -> false
		}

	val isStatic get() = false
}

data class EmptyOptions(val empty: Empty) : Options() {
	override fun toString() = super.toString()
}

data class LinkOptions(val link: OptionsLink) : Options() {
	override fun toString() = super.toString()
}

fun options(empty: Empty): Options = EmptyOptions(empty)
fun options(link: OptionsLink): Options = LinkOptions(link)

fun options(vararg items: TypeItem) = options(empty).fold(items) { plus(it) }
fun options(line: TypeLine, vararg lines: TypeLine) = options(empty).plus(line).fold(lines) { plus(it) }
fun options(name: String, vararg names: String) = options(empty).plus(name).fold(names) { plus(it) }

tailrec fun Options.plusReversed(options: Options): Options =
	when (options) {
		is EmptyOptions -> this
		is LinkOptions -> plus(options.link.item).plusReversed(options.link.lhs)
	}