package leo14.untyped

sealed class Body
data class ConstantBody(val constant: Constant) : Body()
data class FunctionBody(val function: Function) : Body()

fun body(constant: Constant): Body = ConstantBody(constant)
fun body(function: Function): Body = FunctionBody(function)

val Constant.body: Body get() = ConstantBody(this)

fun Body.apply(program: Program) =
	when (this) {
		is ConstantBody -> constant.program
		is FunctionBody -> function.apply(program)
	}
