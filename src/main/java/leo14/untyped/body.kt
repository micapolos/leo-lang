package leo14.untyped

import leo14.Script

sealed class Body
data class ProgramBody(val program: Program) : Body()
data class ScriptBody(val script: Script) : Body()

fun body(program: Program): Body = ProgramBody(program)
fun body(script: Script): Body = ScriptBody(script)

fun Body.apply(context: Context, given: Program): Thunk =
	when (this) {
		is ProgramBody -> thunk(program)
		is ScriptBody -> function(context, script).apply(given)
	}
