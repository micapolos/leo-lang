package leo13.type

import leo.base.notNullOrError
import leo13.ObjectScripting
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.plus
import leo13.script.script
import leo13.traceName

data class TypeTrace(val lhsOrNull: TypeTrace?, val line: TypeLine) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = traceName lineTo (lhsOrNull?.scriptingLine?.rhs ?: script()).plus(line.scriptingLine.rhs)

	fun plus(item: TypeItem) =
		when (item) {
			is LineTypeItem -> plus(item.line)
			is RecurseTypeItem -> plus(item.recurse)
		}

	fun plus(line: TypeLine) =
		TypeTrace(this, line)

	fun plus(recurse: Recurse): TypeTrace =
		if (recurse.lhsOrNull == null) this
		else lhsOrNull.notNullOrError("recurse").run { plus(recurse.lhsOrNull) }

}

fun trace(line: TypeLine) = TypeTrace(null, line)

fun TypeTrace?.orNullPlus(item: TypeItem) =
	this?.plus(item).notNullOrError("recurse")

fun TypeTrace?.orNullPlus(line: TypeLine) =
	this?.plus(line) ?: trace(line)

fun TypeTrace?.orNullPlus(recurse: Recurse): TypeTrace =
	notNullOrError("recurse").plus(recurse)
