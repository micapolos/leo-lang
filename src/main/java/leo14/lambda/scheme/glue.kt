package leo14.lambda.scheme

import leo.base.string
import leo13.Index
import leo13.index0
import leo13.index1
import leo13.index2
import leo14.lambda.Term
import leo14.lambda.term
import leo14.literal

typealias Term = Term<Code>

fun arg(index: Index): Term<Code> = leo14.lambda.arg(index)
val arg0 get() = arg(index0)
val arg1 get() = arg(index1)
val arg2 get() = arg(index2)

fun term(code: Code) = term(code)
fun term(int: Int) = term(code(literal(int).string))
fun term(double: Double) = term(literal(double).string)
fun term(string: String) = term(literal(string).string)
