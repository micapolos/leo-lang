package leo13.type

import leo.base.notNullIf
import leo13.ObjectScripting
import leo13.linkName
import leo13.script.lineTo
import leo13.script.plus

data class TypeLink(val lhs: Type, val item: TypeItem) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			linkName lineTo lhs.scriptingLine.rhs.plus(item.scriptingLine.rhs)

	val type get() = lhs.plus(item)
	val onlyItemOrNull get() = notNullIf(lhs.isEmpty) { item }
	val onlyLineOrNull get() = onlyItemOrNull?.line
	val line get() = item.line

	fun plus(item: TypeItem) =
		type(this) linkTo item

	fun plus(line: TypeLine) =
		plus(item(line))

	fun typeOrNull(name: String): Type? =
		line.rhsOrNull(name)?.let { type(name lineTo it) } ?: lhs.typeOrNull(name)

	fun setLineRhsOrNull(line: TypeLine): TypeLink? =
		line.setRhsOrNull(line)
			?.let { lhs linkTo it }
			?: lhs.setLineRhsOrNull(line)?.let { it linkTo line }

	fun leafPlusOrNull(type: Type): TypeLink? =
		line.leafPlusOrNull(type)?.let { lhs linkTo it }

	fun getOrNull(name: String): Type? =
		line.rhs.typeOrNull(name)

	fun setOrNull(setLine: TypeLine): TypeLink? =
		line.rhs.setLineRhsOrNull(setLine)?.let { lhs linkTo (line.name lineTo it) }

	fun expand(rootOrNull: RecurseRoot?): TypeLink =
		lhs.expand(rootOrNull) linkTo item.expand(rootOrNull)

	fun contains(link: TypeLink, traceOrNull: TypeTrace?): Boolean =
		lhs.contains(link.lhs, traceOrNull) && item.contains(link.item, traceOrNull)
}

infix fun Type.linkTo(item: TypeItem) = TypeLink(this, item)
infix fun Type.linkTo(line: TypeLine) = linkTo(item(line))
