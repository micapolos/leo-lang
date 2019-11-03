package leo14.lambda.java

import leo13.Index
import leo13.index0
import leo13.index1
import leo13.index2
import leo14.lambda.Value

typealias Value = Value<Native>

fun arg(index: Index): Value<Native> = leo14.lambda.arg(index)
val arg0 get() = arg(index0)
val arg1 get() = arg(index1)
val arg2 get() = arg(index2)

fun value(int: Int) = leo14.lambda.value(native(int))
fun value(double: Double) = leo14.lambda.value(native(double))
fun value(string: String) = leo14.lambda.value(native(string))
