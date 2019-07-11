package lambda.v2

import leo.base.nat

data class Body(val arity: Int)
fun body(arity: Int) = Body(arity)

fun Body.arg(index: Int) = term(argument(arity.minus(index).nat))