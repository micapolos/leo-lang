package leo13.untyped

import leo.base.notNullIf
import leo13.LeoStruct
import leo13.script.Script
import leo13.script.ScriptLine

data class case(val name: name, val rhs: Script) : LeoStruct("case", name, rhs) {
	override fun toString() = super.toString()
}

fun case.rhsOrNull(line: ScriptLine): Script? =
	notNullIf(name.string == line.name) { rhs }