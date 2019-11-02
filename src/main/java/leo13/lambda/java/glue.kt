package leo13.lambda.java

import leo13.index
import leo13.lambda.Value

typealias Value = Value<Native>

fun arg(int: Int): Value<Native> = leo13.lambda.arg(int.index)
val arg0 get() = arg(0)
val arg1 get() = arg(1)
val arg2 get() = arg(2)

fun value(int: Int) = leo13.lambda.value(native(int))
fun value(double: Double) = leo13.lambda.value(native(double))
fun value(string: String) = leo13.lambda.value(native(string))
