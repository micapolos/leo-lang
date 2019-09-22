package leo13.pattern

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

	fun setOrNull(recurse: Recurse) = lhsOrNull?.plusOrNull(recurse)

	fun setOrNull(pattern: Pattern) =
		when (pattern) {
			is NodePattern -> set(pattern.node)
			is RecursePattern -> setOrNull(pattern.recurse)
		}

	fun plus(line: PatternLine) =
		set(node.plus(line))

	fun plus(node: PatternNode) = PatternTrace(this, node)

	fun plusOrNull(pattern: Pattern): PatternTrace? =
		when (pattern) {
			is RecursePattern -> plusOrNull(pattern.recurse)
			is NodePattern -> plus(pattern.node)
		}

	fun plusOrNull(recurse: Recurse): PatternTrace? =
		if (recurse.lhsOrNull == null) this
		else lhsOrNull?.run { plusOrNull(recurse.lhsOrNull) }

	val previousOrNull: PatternTrace?
		get() =
			node.previousOrNull?.let { setOrNull(it) }

	val contentOrNull: PatternTrace?
		get() =
		node.linkOrNull?.run { plusOrNull(line.rhs) }

	fun getOrNull(name: String): PatternTrace? =
		node.getOrNull(name)?.let { plusOrNull(it) }
}

fun trace(node: PatternNode) = PatternTrace(null, node)
