package leo13.pattern

import leo13.ObjectScripting
import leo13.linkName
import leo13.script.lineTo
import leo13.script.plus

data class OptionsLink(val lhs: Options, val line: PatternLine) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			linkName lineTo lhs.scriptingLine.rhs.plus(line.scriptingLine.rhs)

	fun contains(link: PatternLink) =
		link.lhs.isEmpty && contains(link.line)

	fun contains(line: PatternLine): Boolean =
		this.line.contains(line) || lhs.contains(line)

	fun contains(link: OptionsLink) =
		line.contains(link.line) && lhs.contains(link.lhs)

	fun recurseExpand(rootRecurse: Recurse?, rootNode: PatternNode): OptionsLink =
		lhs.recurseExpand(rootRecurse, rootNode) linkTo line.recurseExpand(rootRecurse, rootNode)

	fun recurseContains(link: OptionsLink, trace: PatternTrace): Boolean =
		lhs.recurseContains(link.lhs, trace) && line.recurseContains(link.line, trace)

	fun recurseContains(line: PatternLine, trace: PatternTrace): Boolean =
		this.line.recurseContains(line, trace) || lhs.recurseContains(line, trace)
}

infix fun Options.linkTo(line: PatternLine) = OptionsLink(this, line)