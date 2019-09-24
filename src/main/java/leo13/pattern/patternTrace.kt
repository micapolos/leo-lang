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
		get() = traceName lineTo (lhsOrNull?.scriptingLine?.rhs ?: script()).plus(node.scriptingLine.rhs)

	fun plus(pattern: Pattern) =
		when (pattern) {
			is NodePattern -> plus(pattern.node)
			is RecursePattern -> plus(pattern.recurse)
		}

	fun plus(node: PatternNode) =
		PatternTrace(this, node)

	fun plus(recurse: Recurse): PatternTrace =
		if (recurse.lhsOrNull == null) this
		else lhsOrNull.notNullOrError("recurse").run { plus(recurse.lhsOrNull) }

}

fun trace(node: PatternNode) = PatternTrace(null, node)

fun PatternTrace?.orNullPlus(node: PatternNode) =
	this?.plus(node) ?: trace(node)

fun PatternTrace?.orNullPlus(recurse: Recurse): PatternTrace =
	notNullOrError("recurse").plus(recurse)
