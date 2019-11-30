package leo14

import leo14.typed.compiler.Compiler
import leo14.typed.evaluator.Evaluator

sealed class TokenReader

data class CompilerTokenReader<T>(val compiler: Compiler<T>) : TokenReader()
data class EvaluatorTokenReader<T>(val evaluator: Evaluator<T>) : TokenReader()

val <T> Compiler<T>.tokenReader: TokenReader get() = CompilerTokenReader(this)
val <T> Evaluator<T>.tokenReader: TokenReader get() = EvaluatorTokenReader(this)
