package leo13.script

import leo.base.notNullIf
import leo.base.updateIf
import leo13.LeoObject
import leo13.fail
import leo13.untyped.metaName

data class ScriptLine(val name: String, val rhs: Script) : LeoObject() {
	override fun toString() = script(this).toString()
	override val scriptableName get() = "line"
	override val scriptableBody get() = script(name lineTo script("to" lineTo rhs))
}

val ScriptLine.onlyNameOrNull: String?
	get() =
		notNullIf(rhs.isEmpty) { name }

val ScriptLine.unsafeOnlyName: String
	get() =
		if (rhs.isEmpty) name
		else fail("expected" lineTo script("name"))

fun ScriptLine.unsafeRhs(name: String): Script =
	if (this.name == name) rhs
	else fail(
		script(
			name lineTo script(),
			"expected" lineTo script()))

fun ScriptLine.metaFor(vararg names: String): ScriptLine =
	updateIf(names.contains(this.name)) { meta }

val ScriptLine.meta get()  =
	metaName lineTo script(this)
