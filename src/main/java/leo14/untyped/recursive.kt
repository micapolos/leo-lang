package leo14.untyped

data class Recursive(val function: Function)

val Function.recursive get() = Recursive(this)