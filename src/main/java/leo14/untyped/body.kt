package leo14.untyped

sealed class Body
data class ProgramBody(val program: Program) : Body()
data class FunctionBody(val function: Function) : Body()

fun body(program: Program): Body = ProgramBody(program)
fun body(function: Function): Body = FunctionBody(function)

fun Body.apply(program: Program) =
	when (this) {
		is ProgramBody -> this.program
		is FunctionBody -> function.apply(program)
	}
