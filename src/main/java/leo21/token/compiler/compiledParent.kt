package leo21.token.compiler

import leo21.compiled.Compiled
import leo21.compiled.lineTo
import leo21.token.processor.CompilerTokenProcessor
import leo21.token.processor.TokenProcessor

sealed class CompiledParent
data class CompilerNameCompiledParent(val compiler: TokenCompiler, val name: String) : CompiledParent()
data class CompilerDoCompiledParent(val compiler: TokenCompiler) : CompiledParent()

fun CompiledParent.plus(compiled: Compiled): TokenProcessor =
	when (this) {
		is CompilerNameCompiledParent ->
			CompilerTokenProcessor(compiler.plus(name lineTo compiled))
		is CompilerDoCompiledParent ->
			CompilerTokenProcessor(compiler.plusDo(compiled))
	}