package leo13.lambda.eval

import leo13.Stack

typealias Value = leo13.lambda.Value<Any>
typealias Stack = Stack<Any>

fun arg(index: Int): Value = leo13.lambda.arg(index)
val arg0 get() = arg(0)
val arg1 get() = arg(1)
val arg2 get() = arg(2)
