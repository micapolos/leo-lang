package leo13.pattern

import leo.base.notNullOrError
import leo13.ObjectScripting
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.plus
import leo13.script.script
import leo13.traceName

data class PatternTrace(val lhsOrNull: PatternTrace?, val line: PatternLine) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = traceName lineTo (lhsOrNull?.scriptingLine?.rhs ?: script()).plus(line.scriptingLine.rhs)

	fun plus(line: PatternLine) =
		PatternTrace(resolveRhs, line)

	fun plus(recurse: Recurse): PatternTrace =
		if (recurse.lhsOrNull == null) this
		else lhsOrNull!!.run { plus(recurse.lhsOrNull) }

	val resolveRhs: PatternTrace get() =
		when (line.rhs) {
			is NodePattern -> this
			is RecursePattern -> plus(line.rhs.recurse)
		}
}

fun trace(line: PatternLine) = PatternTrace(null, line)

fun PatternTrace?.orNullPlus(line: PatternLine) =
	this?.plus(line) ?: trace(line)

fun PatternTrace?.orNullPlus(recurse: Recurse): PatternTrace =
	notNullOrError("recurse").plus(recurse)
