package leo14.untyped

import leo14.Script
import leo14.ScriptLink
import leo14.script

sealed class Body
data class ScriptBody(val script: Script) : Body()
data class FunctionBody(val function: Function) : Body()

fun body(script: Script): Body = ScriptBody(script)
fun body(function: Function): Body = FunctionBody(function)

fun Body.apply(scriptLink: ScriptLink) =
	when (this) {
		is ScriptBody -> script
		is FunctionBody -> function.apply(script(scriptLink))
	}
