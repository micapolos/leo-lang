package leo21.token.processor

import leo13.fold
import leo13.reverse
import leo14.Script
import leo14.Token
import leo14.tokenStack
import leo21.compiled.Compiled
import leo21.token.body.Body
import leo21.token.body.BodyCompiler
import leo21.token.body.DefineCompiler
import leo21.token.body.FunctionCompiler
import leo21.token.body.FunctionItCompiler
import leo21.token.body.FunctionItDoesCompiler
import leo21.token.body.emptyBodyCompiler
import leo21.token.body.plus
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
data class TypeCompilerTokenProcessor(val typeCompiler: TypeCompiler) : TokenProcessor()
data class ChoiceCompilerTokenProcessor(val choiceCompiler: ChoiceCompiler) : TokenProcessor()
data class ArrowCompilerTokenProcessor(val arrowCompiler: ArrowCompiler) : TokenProcessor()
data class ScriptCompilerTokenProcessor(val scriptCompiler: ScriptCompiler) : TokenProcessor()
data class BodyCompilerTokenProcessor(val bodyCompiler: BodyCompiler) : TokenProcessor()
data class FunctionCompilerTokenProcessor(val functionCompiler: FunctionCompiler) : TokenProcessor()
data class FunctionItCompilerTokenProcessor(val functionItCompiler: FunctionItCompiler) : TokenProcessor()
data class FunctionItDoesCompilerTokenProcessor(val functionItDoesCompiler: FunctionItDoesCompiler) : TokenProcessor()
data class DefineCompilerTokenProcessor(val defineCompiler: DefineCompiler) : TokenProcessor()

val emptyTyperTokenProcessor: TokenProcessor =
	TypeCompilerTokenProcessor(emptyTypeCompiler)

val emptyScriptTokenProcessor: TokenProcessor =
	ScriptCompilerTokenProcessor(emptyScriptCompiler)

val emptyBodyTokenProcessor: TokenProcessor =
	BodyCompilerTokenProcessor(emptyBodyCompiler)

val BodyCompiler.asTokenProcessor: TokenProcessor get() = BodyCompilerTokenProcessor(this)
val FunctionCompiler.asTokenProcessor: TokenProcessor get() = FunctionCompilerTokenProcessor(this)
val FunctionItCompiler.asTokenProcessor: TokenProcessor get() = FunctionItCompilerTokenProcessor(this)
val FunctionItDoesCompiler.asTokenProcessor: TokenProcessor get() = FunctionItDoesCompilerTokenProcessor(this)

fun TokenProcessor.plus(token: Token): TokenProcessor =
	when (this) {
		is TypeCompilerTokenProcessor -> typeCompiler.plus(token)
		is ChoiceCompilerTokenProcessor -> choiceCompiler.plus(token)
		is ArrowCompilerTokenProcessor -> arrowCompiler.plus(token)
		is ScriptCompilerTokenProcessor -> scriptCompiler.plus(token)
		is BodyCompilerTokenProcessor -> bodyCompiler.plus(token)
		is FunctionCompilerTokenProcessor -> functionCompiler.plus(token)
		is FunctionItCompilerTokenProcessor -> functionItCompiler.plus(token)
		is FunctionItDoesCompilerTokenProcessor -> functionItDoesCompiler.plus(token)
		is DefineCompilerTokenProcessor -> defineCompiler.plus(token)
	}

fun TokenProcessor.plus(script: Script): TokenProcessor =
	fold(script.tokenStack.reverse) { plus(it) }

val TokenProcessor.type: Type
	get() =
		(this as TypeCompilerTokenProcessor).typeCompiler.type

val TokenProcessor.body: Body
	get() =
		(this as BodyCompilerTokenProcessor).bodyCompiler.body

val TokenProcessor.compiled: Compiled
	get() =
		body.compiled
