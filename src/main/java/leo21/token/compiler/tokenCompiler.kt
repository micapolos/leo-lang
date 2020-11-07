package leo21.token.compiler

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo21.compiled.Compiled
import leo21.compiled.LineCompiled
import leo21.compiled.compiled
import leo21.compiler.Compiler
import leo21.compiler.emptyBindings
import leo21.compiler.plus
import leo21.compiler.plusDo
import leo21.compiler.plusRaw
import leo21.compiler.push
import leo21.compiler.resolve
import leo21.token.processor.CompilerTokenProcessor
import leo21.token.processor.TokenProcessor

data class TokenCompiler(
	val parentOrNull: CompiledParent?,
	val lineCompiler: Compiler
)

val emptyTokenCompiler = TokenCompiler(null, Compiler(emptyBindings, compiled()))

fun TokenCompiler.plus(token: Token): TokenProcessor =
	when (token) {
		is LiteralToken ->
			CompilerTokenProcessor(
				copy(lineCompiler = lineCompiler.plus(token.literal)))
		is BeginToken ->
			when (token.begin.string) {
				"do" ->
					CompilerTokenProcessor(
						TokenCompiler(
							CompilerDoCompiledParent(this),
							Compiler(
								lineCompiler.bindings.push(lineCompiler.compiled.type),
								compiled())))
				"function" -> TODO()
				"make" -> TODO()
				else -> CompilerTokenProcessor(
					TokenCompiler(
						CompilerNameCompiledParent(this, token.begin.string),
						Compiler(lineCompiler.bindings, compiled())))
			}
		is EndToken ->
			parentOrNull!!.plus(lineCompiler.compiled)
	}

fun TokenCompiler.plus(compiled: LineCompiled): TokenCompiler =
	copy(lineCompiler = lineCompiler.plusRaw(compiled).resolve)

fun TokenCompiler.plusDo(compiled: Compiled): TokenCompiler =
	copy(lineCompiler = lineCompiler.plusDo(compiled))