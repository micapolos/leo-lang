package leo13.lambda.java

import leo13.lambda.Value
import leo13.lambda.value

fun arg(index: Int): Value<Native> = leo13.lambda.arg(index)
val arg0 get() = arg(0)
val arg1 get() = arg(1)
val arg2 get() = arg(2)

fun value(int: Int) = value(native(int))
fun value(double: Double) = value(native(double))
fun value(string: String) = value(native(string))
