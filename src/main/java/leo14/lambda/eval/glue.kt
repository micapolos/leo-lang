package leo14.lambda.eval

import leo13.*
import leo13.Stack

typealias Value = leo14.lambda.Value<Any>
typealias Stack = Stack<Any>

fun arg(index: Index): Value = leo14.lambda.arg(index)
val arg0 get() = arg(index0)
val arg1 get() = arg(index1)
val arg2 get() = arg(index2)
