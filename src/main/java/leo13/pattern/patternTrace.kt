package leo13.pattern

import leo.base.notNullOrError
import leo13.ObjectScripting
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.plus
import leo13.script.script
import leo13.traceName

data class PatternTrace(val lhsOrNull: PatternTrace?, val node: PatternNode) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = traceName lineTo (lhsOrNull?.scriptingLine?.rhs ?: script()).plus(node.scriptingLine)

	fun set(node: PatternNode) = PatternTrace(lhsOrNull, node)

	fun set(recurse: Recurse) = lhsOrNull!!.plus(recurse)

	fun set(pattern: Pattern) =
		when (pattern) {
			is NodePattern -> set(pattern.node)
			is RecursePattern -> set(pattern.recurse)
		}

	fun plus(line: PatternLine) =
		set(node.plus(line))

	fun plus(node: PatternNode) = PatternTrace(this, node)

	fun plus(recurse: Recurse): PatternTrace =
		if (recurse.lhsOrNull == null) this
		else lhsOrNull!!.run { plus(recurse.lhsOrNull) }
}

fun trace(node: PatternNode) = PatternTrace(null, node)

fun PatternTrace?.plus(node: PatternNode) =
	this?.plus(node) ?: trace(node)

fun PatternTrace?.plus(recurse: Recurse): PatternTrace =
	notNullOrError("recurse").plus(recurse)
