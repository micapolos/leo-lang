package leo14.js.compiler

import leo14.Compiler

data class Fallback(val compiler: Compiler)

fun fallback(compiler: Compiler) = Fallback(compiler)