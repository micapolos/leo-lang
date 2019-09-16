package leo13.untyped.expression

import leo13.untyped.compiler.Compiled

sealed class CompilerToken

data class BeginCompilerToken(val name: String) : CompilerToken()
data class CompiledCompilerToken(val compiled: Compiled) : CompilerToken()