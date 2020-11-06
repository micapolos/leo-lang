package leo21.token.processor

import leo13.fold
import leo13.reverse
import leo14.Script
import leo14.Token
import leo14.tokenStack
import leo21.compiled.Compiled
import leo21.token.compiler.TokenCompiler
import leo21.token.compiler.emptyTokenCompiler
import leo21.token.compiler.plus

sealed class TokenProcessor
data class CompilerTokenProcessor(val compiler: TokenCompiler) : TokenProcessor()

val emptyCompilerTokenProcessor: TokenProcessor =
	CompilerTokenProcessor(emptyTokenCompiler)

fun TokenProcessor.plus(token: Token): TokenProcessor =
	when (this) {
		is CompilerTokenProcessor -> compiler.plus(token)
	}

fun TokenProcessor.plus(script: Script): TokenProcessor =
	fold(script.tokenStack.reverse) { plus(it) }

val TokenProcessor.compiled: Compiled
	get() =
		(this as CompilerTokenProcessor).compiler.lineCompiler.compiled
