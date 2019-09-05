package leo13.untyped

import leo.base.notNullIf
import leo13.LeoStruct
import leo13.script.Script
import leo13.script.ScriptLine

data class Case(val name: Name, val rhs: Script) : LeoStruct("case", name, rhs) {
	override fun toString() = super.toString()
}

infix fun String.caseTo(rhs: Script) = Case(name(this), rhs)

fun Case.rhsOrNull(line: ScriptLine): Script? =
	notNullIf(name.string == line.name) { rhs }