package leo14.untyped

import leo14.Script

data class Function(
	val context: Context,
	val script: Script)

fun Context.function(script: Script) = Function(this, script)

fun Function.apply(param: Script) =
	context.push(param.givenRule).eval(script)