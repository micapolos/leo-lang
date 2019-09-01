package leo13.type

import leo.base.notNullIf
import leo13.LeoObject
import leo13.script.plus

data class TypeLink(val lhs: Type, val line: TypeLine) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "link"
	override val scriptableBody get() = lhs.scriptableBody.plus(line.scriptableLineWithMeta(needsMeta(line.name)))
	fun needsMeta(name: String): Boolean =
		when (name) {
			"or" -> lhs is LinkType && lhs.link.lhs is EmptyType
			"to" -> lhs is LinkType
			else -> false
		}
}

fun link(lhs: Type, line: TypeLine) = TypeLink(lhs, line)
fun TypeLink.plus(line: TypeLine): TypeLink = link(type(this), line)
val TypeLink.onlyLineOrNull: TypeLine? get() = notNullIf(lhs.isEmpty) { line }

fun TypeLink.contains(link: TypeLink): Boolean =
	lhs.contains(link.lhs) && line.contains(link.line)

