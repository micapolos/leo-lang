package leo13.expression

import leo13.compiler.TypedExpression

sealed class CompilerToken

data class BeginCompilerToken(val name: String) : CompilerToken()
data class CompiledCompilerToken(val typedExpression: TypedExpression) : CompilerToken()