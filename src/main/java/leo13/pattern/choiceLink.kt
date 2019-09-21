package leo13.pattern

import leo13.ObjectScripting
import leo13.script.lineTo

data class ChoiceLink(val lhs: Choice, val line: PatternLine) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			"choice" lineTo lhs.scriptingLine.rhs.plus("either" lineTo line.scriptingLine.rhs)

	fun contains(link: PatternLink) =
		link.lhs.isEmpty && contains(link.line)

	fun contains(line: PatternLine): Boolean =
		this.line.contains(line) || lhs.contains(line)

	fun contains(link: ChoiceLink) =
		line.contains(link.line) && lhs.contains(link.lhs)
}

infix fun Choice.linkTo(line: PatternLine) = ChoiceLink(this, line)