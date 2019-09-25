package leo13.type

import leo13.ObjectScripting
import leo13.rootName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class RecurseRoot(val recurse: Recurse, val line: TypeLine) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = rootName lineTo script(recurse.scriptingLine, line.scriptingLine)

	val recurseIncrease get() = RecurseRoot(recurse.increase, line)
}

fun root(recurse: Recurse, line: TypeLine) = RecurseRoot(recurse, line)

fun RecurseRoot?.orNullRecurseIncrease(line: TypeLine): RecurseRoot =
	this?.recurseIncrease ?: root(onceRecurse, line)