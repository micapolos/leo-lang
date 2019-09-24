package leo13.pattern

import leo.base.notNullIf
import leo13.ObjectScripting
import leo13.linkName
import leo13.script.lineTo
import leo13.script.plus

data class PatternLink(val lhs: Pattern, val line: PatternLine) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			linkName lineTo lhs.scriptingLine.rhs.plus(line.scriptingLine.rhs)

	val pattern get() = lhs.plus(line)
	val onlyLineOrNull get() = notNullIf(lhs.isEmpty) { line }

	fun plus(line: PatternLine) =
		pattern(node(this)) linkTo line

	fun lineRhsOrNull(name: String): Pattern? =
		line.rhsOrNull(name) ?: lhs.lineRhsOrNull(name)

	fun setLineRhsOrNull(line: PatternLine): PatternLink? =
		line.setRhsOrNull(line)
			?.let { lhs linkTo it }
			?: lhs.setLineRhsOrNull(line)?.let { it linkTo line }

	fun leafPlusOrNull(pattern: Pattern): PatternLink? =
		line.leafPlusOrNull(pattern)?.let { lhs linkTo it }

	fun getOrNull(name: String): Pattern? =
		line.recurseExpand().rhs.lineRhsOrNull(name)?.let { pattern(name lineTo it) }

	fun setOrNull(setLine: PatternLine): PatternLink? =
		line.rhs.setLineRhsOrNull(setLine)?.let { lhs linkTo (line.name lineTo it) }

	fun recurseExpand(rootOrNull: RecurseRoot?): PatternLink =
		lhs.recurseExpand(rootOrNull) linkTo line.recurseExpand(rootOrNull.orNullRecurseIncrease(node(this)))

	fun contains(link: PatternLink, trace: PatternTrace?): Boolean =
		lhs.contains(link.lhs, trace) && line.contains(link.line, trace.orNullPlus(node(this)))
}

infix fun Pattern.linkTo(line: PatternLine) = PatternLink(this, line)
