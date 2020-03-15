package leo14.untyped

data class Function(
	val context: Context,
	val program: Program)

fun function(context: Context, program: Program) = Function(context, program)

fun Function.apply(param: Program) =
	context.push(param.givenRule).eval(program)
