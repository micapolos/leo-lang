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
import leo21.token.type.compiler.TokenArrowCompiler
import leo21.token.type.compiler.TokenChoiceCompiler
import leo21.token.type.compiler.TokenTypeCompiler
import leo21.token.type.compiler.emptyTokenTypeCompiler
import leo21.token.type.compiler.plus
import leo21.type.Type

sealed class TokenProcessor
data class CompilerTokenProcessor(val compiler: TokenCompiler) : TokenProcessor()
data class EvaluatorTokenProcessor(val evaluator: TokenEvaluator) : TokenProcessor()
data class TypeCompilerTokenProcessor(val typeCompiler: TokenTypeCompiler) : TokenProcessor()
data class ChoiceCompilerTokenProcessor(val choiceCompiler: TokenChoiceCompiler) : TokenProcessor()
data class ArrowCompilerTokenProcessor(val arrowCompiler: TokenArrowCompiler) : TokenProcessor()

val emptyCompilerTokenProcessor: TokenProcessor =
	CompilerTokenProcessor(emptyTokenCompiler)

val emptyEvaluatorTokenProcessor: TokenProcessor =
	EvaluatorTokenProcessor(emptyTokenEvaluator)

val emptyTyperTokenProcessor: TokenProcessor =
	TypeCompilerTokenProcessor(emptyTokenTypeCompiler)

fun TokenProcessor.plus(token: Token): TokenProcessor =
	when (this) {
		is CompilerTokenProcessor -> compiler.plus(token)
		is EvaluatorTokenProcessor -> evaluator.plus(token)
		is TypeCompilerTokenProcessor -> typeCompiler.plus(token)
		is ChoiceCompilerTokenProcessor -> choiceCompiler.plus(token)
		is ArrowCompilerTokenProcessor -> arrowCompiler.plus(token)
	}

fun TokenProcessor.plus(script: Script): TokenProcessor =
	fold(script.tokenStack.reverse) { plus(it) }

val TokenProcessor.compiled: Compiled
	get() =
		(this as CompilerTokenProcessor).compiler.lineCompiler.compiled

val TokenProcessor.evaluated: Evaluated
	get() =
		(this as EvaluatorTokenProcessor).evaluator.evaluator.evaluated

val TokenProcessor.type: Type
	get() =
		(this as TypeCompilerTokenProcessor).typeCompiler.type
