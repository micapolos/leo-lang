package leo13.js.compiler

data class Invoke(val index: Int, val arg: Expression)

fun invoke(index: Int, arg: Expression) = Invoke(index, arg)
val Invoke.code get() = "fn$index(${arg.code})"