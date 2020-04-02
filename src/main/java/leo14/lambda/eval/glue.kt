package leo14.lambda.eval

import leo13.Index
import leo13.Stack

typealias Term = leo14.lambda.Term<Any>
typealias Stack = Stack<Any>

fun arg(index: Index): Term = leo14.lambda.arg(index)
val arg0 get() = arg(0)
val arg1 get() = arg(1)
val arg2 get() = arg(2)
