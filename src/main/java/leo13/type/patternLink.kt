package leo13.type

import leo.base.notNullIf
import leo13.Scriptable
import leo13.script.plus

data class PatternLink(val lhs: Pattern, val line: PatternLine) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "link"
	override val scriptableBody get() = lhs.scriptableBody.plus(line.scriptableLineWithMeta(needsMeta(line.name)))
	fun needsMeta(name: String): Boolean =
		when (name) {
			"or" -> lhs is LinkPattern && lhs.link.lhs is EmptyPattern
			"to" -> lhs is LinkPattern
			else -> false
		}
}

fun link(lhs: Pattern, line: PatternLine) = PatternLink(lhs, line)
fun PatternLink.plus(line: PatternLine): PatternLink = link(pattern(this), line)
val PatternLink.onlyLineOrNull: PatternLine? get() = notNullIf(lhs.isEmpty) { line }

fun PatternLink.contains(link: PatternLink): Boolean =
	lhs.contains(link.lhs) && line.contains(link.line)

