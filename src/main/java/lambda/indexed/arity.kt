package lambda.indexed

import leo.base.nat

data class Arity(val int: Int)

fun arity(int: Int) = Arity(int)

fun Arity.arg(index: Int) = term(argument(int.minus(index).nat))