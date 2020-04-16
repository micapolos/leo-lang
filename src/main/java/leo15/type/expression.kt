package leo15.type

import leo15.lambda.Term

data class Expression(val term: Term, val isConstant: Boolean)

val Term.constantExpression get() = Expression(this, isConstant = true)
val Term.dynamicExpression get() = Expression(this, isConstant = false)
