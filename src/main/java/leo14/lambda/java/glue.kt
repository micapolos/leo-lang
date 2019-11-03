package leo14.lambda.java

import leo13.Index
import leo13.index0
import leo13.index1
import leo13.index2
import leo14.lambda.Term

typealias Term = Term<Native>

fun arg(index: Index): Term<Native> = leo14.lambda.arg(index)
val arg0 get() = arg(index0)
val arg1 get() = arg(index1)
val arg2 get() = arg(index2)

fun term(int: Int) = leo14.lambda.term(native(int))
fun term(double: Double) = leo14.lambda.term(native(double))
fun term(string: String) = leo14.lambda.term(native(string))
