package leo21.token.processor

import leo13.fold
import leo13.reverse
import leo14.Script
import leo14.Token
import leo14.tokenStack
import leo21.compiled.Compiled
import leo21.evaluator.Evaluated
import leo21.token.compiler.TokenCompiler
import leo21.token.compiler.emptyTokenCompiler
import leo21.token.compiler.plus
import leo21.token.evaluator.TokenEvaluator
import leo21.token.evaluator.emptyTokenEvaluator
import leo21.token.evaluator.plus

sealed class TokenProcessor
data class CompilerTokenProcessor(val compiler: TokenCompiler) : TokenProcessor()
data class EvaluatorTokenProcessor(val evaluator: TokenEvaluator) : TokenProcessor()

val emptyCompilerTokenProcessor: TokenProcessor =
	CompilerTokenProcessor(emptyTokenCompiler)

val emptyEvaluatorTokenProcessor: TokenProcessor =
	EvaluatorTokenProcessor(emptyTokenEvaluator)

fun TokenProcessor.plus(token: Token): TokenProcessor =
	when (this) {
		is CompilerTokenProcessor -> compiler.plus(token)
		is EvaluatorTokenProcessor -> evaluator.plus(token)
	}

fun TokenProcessor.plus(script: Script): TokenProcessor =
	fold(script.tokenStack.reverse) { plus(it) }

val TokenProcessor.compiled: Compiled
	get() =
		(this as CompilerTokenProcessor).compiler.lineCompiler.compiled

val TokenProcessor.evaluated: Evaluated
	get() =
		(this as EvaluatorTokenProcessor).evaluator.evaluator.evaluated
