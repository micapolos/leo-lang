package leo14.lambda.scheme

import leo.base.string
import leo13.Index
import leo14.lambda.Term
import leo14.lambda.term
import leo14.literal

typealias Term = Term<Code>

fun arg(index: Index): Term<Code> = leo14.lambda.arg(index)
val arg0 get() = arg(0)
val arg1 get() = arg(1)
val arg2 get() = arg(2)

fun term(code: Code) = term(code)
fun term(int: Int) = term(code(literal(int).string))
fun term(double: Double) = term(literal(double).string)
fun term(string: String) = term(literal(string).string)
