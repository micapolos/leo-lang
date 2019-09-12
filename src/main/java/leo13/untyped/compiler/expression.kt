package leo13.untyped.compiler

sealed class Expression
data class LinkExpression(val link: ExpressionLink) : Expression()