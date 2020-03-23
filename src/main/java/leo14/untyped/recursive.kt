package leo14.untyped

data class Recursive(val context: Context)

fun recursive(context: Context) = Recursive(context)

fun Recursive.apply(context: Context, program: Program): Thunk? =
	TODO()
