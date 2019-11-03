package leo13.js.compiler

data class Fallback(val compiler: Compiler)

fun fallback(compiler: Compiler) = Fallback(compiler)