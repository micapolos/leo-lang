package leo16

import java.lang.Compiler

sealed class Reader

object VoidReader : Reader()
data class EvaluatorReader(val evaluator: Evaluator) : Reader()
data class DictionarianReader(val dictionarian: Dictionarian) : Reader()
data class CompilerReader(val compiler: Compiler) : Reader()

val voidReader: Reader = VoidReader
val Evaluator.reader: Reader get() = EvaluatorReader(this)
val Dictionarian.reader: Reader get() = DictionarianReader(this)
val Compiler.reader: Reader get() = CompilerReader(this)
