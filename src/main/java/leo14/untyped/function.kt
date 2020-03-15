package leo14.untyped

data class Function(
	val context: Context,
	val program: Program)

fun Context.function(program: Program) = Function(this, program)

fun Function.apply(param: Program) =
	context.push(param.givenRule).eval(program)
