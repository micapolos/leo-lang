package leo13.pattern

import leo13.ObjectScripting
import leo13.linkName
import leo13.script.lineTo
import leo13.script.plus

data class OptionsLink(val lhs: Options, val item: PatternItem) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			linkName lineTo lhs.scriptingLine.rhs.plus(item.scriptingLine.rhs)

	fun expand(rootOrNull: RecurseRoot?): OptionsLink =
		lhs.expand(rootOrNull) linkTo item.expand(rootOrNull)

	fun contains(link: OptionsLink, trace: PatternTrace?): Boolean =
		lhs.contains(link.lhs, trace) && item.contains(link.item, trace)

	fun contains(item: PatternItem, traceOrNull: PatternTrace?): Boolean =
		this.item.contains(item, traceOrNull) || lhs.contains(item, traceOrNull)

	fun contains(line: PatternLine, trace: PatternTrace?): Boolean =
		this.item.line.contains(line, trace) || lhs.contains(line, trace)
}

infix fun Options.linkTo(item: PatternItem) = OptionsLink(this, item)
infix fun Options.linkTo(line: PatternLine) = linkTo(item(line))