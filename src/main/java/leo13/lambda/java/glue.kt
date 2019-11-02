package leo13.lambda.java

import leo13.Index
import leo13.index0
import leo13.index1
import leo13.index2
import leo13.lambda.Value

typealias Value = Value<Native>

fun arg(index: Index): Value<Native> = leo13.lambda.arg(index)
val arg0 get() = arg(index0)
val arg1 get() = arg(index1)
val arg2 get() = arg(index2)

fun value(int: Int) = leo13.lambda.value(native(int))
fun value(double: Double) = leo13.lambda.value(native(double))
fun value(string: String) = leo13.lambda.value(native(string))
