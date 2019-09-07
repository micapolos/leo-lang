package leo13.script

import leo.base.notNullIf
import leo13.LeoObject
import leo13.fail

data class ScriptLine(val name: String, val rhs: Script) : LeoObject() {
	override fun toString() = script(this).toString()
	override val scriptableName get() = "line"
	override val scriptableBody get() = script(name lineTo script("to" lineTo rhs))
}

val ScriptLine.onlyNameOrNull: String?
	get() =
		notNullIf(rhs.isEmpty) { name }

fun ScriptLine.unsafeRhs(name: String): Script =
	if (this.name == name) rhs
	else fail(
		script(
			name lineTo script(),
			"expected" lineTo script()))

