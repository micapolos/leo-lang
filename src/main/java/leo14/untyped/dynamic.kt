package leo14.untyped

sealed class Dynamic
data class FunctionDynamic(val function: Function) : Dynamic()
data class RecursiveDynamic(val recursive: Recursive) : Dynamic()

val Function.dynamic: Dynamic get() = FunctionDynamic(this)
val Recursive.dynamic: Dynamic get() = RecursiveDynamic(this)
