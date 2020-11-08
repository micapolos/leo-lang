package leo21.token.compiler

import leo13.Stack
import leo13.fold
import leo13.push
import leo13.reverse
import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo21.compiled.LineCompiled
import leo21.compiled.compiled
import leo21.compiled.lineCompiled
import leo21.compiler.Bindings
import leo21.compiler.Compiler
import leo21.token.processor.CompilerTokenProcessor
import leo21.token.processor.DataCompilerTokenProcessor
import leo21.token.processor.TokenProcessor

data class DataCompiler(
	val parent: TokenCompiler,
	val bindings: Bindings,
	val lineCompiledStack: Stack<LineCompiled>
)

fun DataCompiler.plus(token: Token): TokenProcessor =
	when (token) {
		is LiteralToken -> DataCompilerTokenProcessor(
			plus(lineCompiled(token.literal)))
		is BeginToken -> CompilerTokenProcessor(
			TokenCompiler(
				DataCompilerNameCompiledParent(this, token.begin.string),
				Compiler(bindings, compiled())))
		is EndToken -> CompilerTokenProcessor(
			parent.fold(lineCompiledStack.reverse) { plusData(it) })
	}

fun DataCompiler.plus(lineCompiled: LineCompiled): DataCompiler =
	copy(lineCompiledStack = lineCompiledStack.push(lineCompiled))