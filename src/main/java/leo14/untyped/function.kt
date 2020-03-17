package leo14.untyped

data class Function(
	val context: Context,
	val program: Program)

fun function(context: Context, program: Program) = Function(context, program)

fun function(program: Program) = function(context(), program)

fun Function.apply(param: Program): Program =
	context
		.push(program(value(this)).thisRule)
		.resolver(param)
		.eval(program)
		.program
