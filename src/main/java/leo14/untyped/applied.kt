package leo14.untyped

import leo14.Script

sealed class Applied
data class ThunkApplied(val thunk: Thunk) : Applied()
data class ScriptApplied(val script: Script) : Applied()

fun applied(thunk: Thunk): Applied = ThunkApplied(thunk)
fun applied(script: Script): Applied = ScriptApplied(script)