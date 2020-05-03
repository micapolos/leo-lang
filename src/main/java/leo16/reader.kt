package leo16

sealed class Reader

data class EvaluatorReader(val evaluator: Evaluator) : Reader()
data class DictionarianReader(val dictionarian: Dictionarian) : Reader()
data class CompilerReader(val compiler: Compiler) : Reader()

val Evaluator.reader: Reader get() = EvaluatorReader(this)
val Dictionarian.reader: Reader get() = DictionarianReader(this)
val Compiler.reader: Reader get() = CompilerReader(this)
