package leo14.untyped

import leo14.Script

sealed class Body
data class ThunkBody(val thunk: Thunk) : Body()
data class ScriptBody(val script: Script) : Body()

fun body(thunk: Thunk): Body = ThunkBody(thunk)
fun body(script: Script): Body = ScriptBody(script)

fun Body.apply(context: Context, given: Thunk): Thunk =
	when (this) {
		is ThunkBody -> thunk
		is ScriptBody -> function(context, script).apply(given)
	}
