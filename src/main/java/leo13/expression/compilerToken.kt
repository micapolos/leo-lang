package leo13.expression

import leo13.compiler.Compiled

sealed class CompilerToken

data class BeginCompilerToken(val name: String) : CompilerToken()
data class CompiledCompilerToken(val compiled: Compiled) : CompilerToken()