package leo9

import leo.base.Empty
import leo.base.empty
import leo.base.fold

sealed class Script

data class EmptyScript(
	val empty: Empty) : Script()

data class ApplicationScript(
	val application: ScriptApplication) : Script()

data class ScriptLine(
	val name: String,
	val script: Script)

data class ScriptApplication(
	val script: Script,
	val line: ScriptLine)

// ------------------

fun script(empty: Empty): Script = EmptyScript(empty)
fun script(application: ScriptApplication): Script = ApplicationScript(application)
fun line(name: String, script: Script) = ScriptLine(name, script)
fun application(script: Script, line: ScriptLine) = ScriptApplication(script, line)

fun script(vararg lines: ScriptLine): Script = script(empty).fold(lines) { script(application(this, it)) }

val Script.applicationOrNull get() = (this as? ApplicationScript)?.application
val Script.application get() = applicationOrNull!!
