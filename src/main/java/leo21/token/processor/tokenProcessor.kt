package leo21.token.processor

import leo13.fold
import leo13.reverse
import leo14.Script
import leo14.Token
import leo14.tokenStack
import leo21.compiled.Compiled
import leo21.evaluator.Evaluated
import leo21.token.body.BodyCompiler
import leo21.token.body.plus
import leo21.token.compiler.DataCompiler
import leo21.token.compiler.FunctionCompiler
import leo21.token.compiler.FunctionTypeCompiler
import leo21.token.compiler.TokenCompiler
import leo21.token.compiler.emptyTokenCompiler
import leo21.token.compiler.plus
import leo21.token.evaluator.TokenEvaluator
import leo21.token.evaluator.emptyTokenEvaluator
import leo21.token.evaluator.plus
import leo21.token.script.ScriptCompiler
import leo21.token.script.emptyScriptCompiler
import leo21.token.script.plus
import leo21.token.type.compiler.ArrowCompiler
import leo21.token.type.compiler.ChoiceCompiler
import leo21.token.type.compiler.TypeCompiler
import leo21.token.type.compiler.emptyTypeCompiler
import leo21.token.type.compiler.plus
import leo21.type.Type

sealed class TokenProcessor
data class CompilerTokenProcessor(val compiler: TokenCompiler) : TokenProcessor()
data class DataCompilerTokenProcessor(val dataCompiler: DataCompiler) : TokenProcessor()
data class EvaluatorTokenProcessor(val evaluator: TokenEvaluator) : TokenProcessor()
data class TypeCompilerTokenProcessor(val typeCompiler: TypeCompiler) : TokenProcessor()
data class ChoiceCompilerTokenProcessor(val choiceCompiler: ChoiceCompiler) : TokenProcessor()
data class ArrowCompilerTokenProcessor(val arrowCompiler: ArrowCompiler) : TokenProcessor()
data class ScriptCompilerTokenProcessor(val scriptCompiler: ScriptCompiler) : TokenProcessor()
data class FunctionTypeCompilerTokenProcessor(val functionTypeCompiler: FunctionTypeCompiler) : TokenProcessor()
data class FunctionCompilerTokenProcessor(val functionCompiler: FunctionCompiler) : TokenProcessor()
data class BodyCompilerTokenProcessor(val bodyCompiler: BodyCompiler) : TokenProcessor()

val emptyCompilerTokenProcessor: TokenProcessor =
	CompilerTokenProcessor(emptyTokenCompiler)

val emptyEvaluatorTokenProcessor: TokenProcessor =
	EvaluatorTokenProcessor(emptyTokenEvaluator)

val emptyTyperTokenProcessor: TokenProcessor =
	TypeCompilerTokenProcessor(emptyTypeCompiler)

val emptyScriptTokenProcessor: TokenProcessor =
	ScriptCompilerTokenProcessor(emptyScriptCompiler)

val BodyCompiler.asTokenProcessor: TokenProcessor get() = BodyCompilerTokenProcessor(this)

fun TokenProcessor.plus(token: Token): TokenProcessor =
	when (this) {
		is CompilerTokenProcessor -> compiler.plus(token)
		is DataCompilerTokenProcessor -> dataCompiler.plus(token)
		is EvaluatorTokenProcessor -> evaluator.plus(token)
		is TypeCompilerTokenProcessor -> typeCompiler.plus(token)
		is ChoiceCompilerTokenProcessor -> choiceCompiler.plus(token)
		is ArrowCompilerTokenProcessor -> arrowCompiler.plus(token)
		is ScriptCompilerTokenProcessor -> scriptCompiler.plus(token)
		is FunctionTypeCompilerTokenProcessor -> functionTypeCompiler.plus(token)
		is FunctionCompilerTokenProcessor -> functionCompiler.plus(token)
		is BodyCompilerTokenProcessor -> bodyCompiler.plus(token)
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
