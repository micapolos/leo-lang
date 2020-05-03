package leo16

data class Compiler(val dictionary: Dictionary, val compiled: Compiled)

fun Dictionary.compiler(compiled: Compiled) = Compiler(this, compiled)
val Dictionary.emptyCompiler get() = compiler(emptyCompiled)