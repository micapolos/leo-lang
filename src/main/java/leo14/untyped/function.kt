package leo14.untyped

import leo14.Script

data class Function(
	val context: Context,
	val script: Script)

fun function(context: Context, script: Script) = Function(context, script)

fun function(script: Script) = function(context(), script)

fun Function.apply(param: Program): Program =
	context
		.push(program(value(this)).thisRule)
		.resolver(param)
		.eval(script)
		.program
