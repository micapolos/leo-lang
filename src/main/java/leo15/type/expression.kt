package leo15.type

import leo15.lambda.Term

import leo15.type.ExpressionKind.CONSTANT
import leo15.type.ExpressionKind.DYNAMIC

enum class ExpressionKind { CONSTANT, DYNAMIC }
data class Expression(val term: Term, val kind: ExpressionKind)

val Term.constantExpression get() = Expression(this, CONSTANT)
val Term.dynamicExpression get() = Expression(this, DYNAMIC)
