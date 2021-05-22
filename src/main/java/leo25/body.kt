package leo25

import leo14.Script

sealed class Body
data class ScriptBody(val script: Script) : Body()
data class FnBody(val fn: Context.(Value) -> Value) : Body()

fun body(script: Script): Body = ScriptBody(script)
fun body(fn: Context.(Value) -> Value): Body = FnBody(fn)

fun Body.apply(context: Context, given: Value): Value =
	when (this) {
		is FnBody -> context.fn(given)
		is ScriptBody -> context.apply(script, given)
	}
