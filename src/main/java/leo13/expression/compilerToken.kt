package leo13.expression

import leo13.compiler.ExpressionTyped

sealed class CompilerToken

data class BeginCompilerToken(val name: String) : CompilerToken()
data class CompiledCompilerToken(val expressionTyped: ExpressionTyped) : CompilerToken()