package leo32

import leo.base.appendableString

data class Script(
	val string: String)

val String.script
	get() =
		Script(this)

fun script(fn: ScriptWriter.() -> ScriptWriter) =
	appendableString { it.scriptWriter.fn() }.script