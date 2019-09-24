package leo13.pattern

import leo.base.notNullIf
import leo13.ObjectScripting
import leo13.linkName
import leo13.script.lineTo
import leo13.script.plus

data class PatternLink(val lhs: Pattern, val item: PatternItem) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			linkName lineTo lhs.scriptingLine.rhs.plus(item.scriptingLine.rhs)

	val pattern get() = lhs.plus(item)
	val onlyItemOrNull get() = notNullIf(lhs.isEmpty) { item }
	val onlyLineOrNull get() = onlyItemOrNull?.line
	val line get() = item.line

	fun plus(item: PatternItem) =
		pattern(this) linkTo item

	fun plus(line: PatternLine) =
		plus(item(line))

	fun lineRhsOrNull(name: String, traceOrNull: PatternTrace? = null): Pattern? =
		line.rhsOrNull(name) ?: lhs.lineRhsOrNull(name)

	fun setLineRhsOrNull(line: PatternLine): PatternLink? =
		line.setRhsOrNull(line)
			?.let { lhs linkTo it }
			?: lhs.setLineRhsOrNull(line)?.let { it linkTo line }

	fun leafPlusOrNull(pattern: Pattern): PatternLink? =
		line.leafPlusOrNull(pattern)?.let { lhs linkTo it }

	fun getOrNull(name: String): Pattern? =
		line.rhs.lineRhsOrNull(name)?.let { pattern(name lineTo it) }

	fun setOrNull(setLine: PatternLine): PatternLink? =
		line.rhs.setLineRhsOrNull(setLine)?.let { lhs linkTo (line.name lineTo it) }

	fun expand(rootOrNull: RecurseRoot?): PatternLink =
		lhs.expand(rootOrNull) linkTo item.expand(rootOrNull)

	fun contains(link: PatternLink, traceOrNull: PatternTrace?): Boolean =
		lhs.contains(link.lhs, traceOrNull) && item.contains(link.item, traceOrNull)
}

infix fun Pattern.linkTo(item: PatternItem) = PatternLink(this, item)
infix fun Pattern.linkTo(line: PatternLine) = linkTo(item(line))
