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

	fun plus(item: PatternItem) =
		when (item) {
			is LinePatternItem -> plus(item.line)
			is RecursePatternItem -> plus(item.recurse)
		}

	fun plus(line: PatternLine) =
		PatternTrace(this, line)

	fun plus(recurse: Recurse): PatternTrace =
		if (recurse.lhsOrNull == null) this
		else lhsOrNull.notNullOrError("recurse").run { plus(recurse.lhsOrNull) }

}

fun trace(line: PatternLine) = PatternTrace(null, line)

fun PatternTrace?.orNullPlus(item: PatternItem) =
	this?.plus(item).notNullOrError("recurse")

fun PatternTrace?.orNullPlus(line: PatternLine) =
	this?.plus(line) ?: trace(line)

fun PatternTrace?.orNullPlus(recurse: Recurse): PatternTrace =
	notNullOrError("recurse").plus(recurse)
