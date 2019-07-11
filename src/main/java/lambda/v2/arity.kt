package lambda.v2

import leo.base.nat

data class Arity(val int: Int)

fun arity(int: Int) = Arity(int)

fun Arity.arg(index: Int) = term(argument(int.minus(index).nat))