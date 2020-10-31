package leo14.lambda.value

sealed class Value
data class FunctionValue(val function: Function) : Value()
data class StringValue(val string: String) : Value()
data class DoubleValue(val double: Double) : Value()
object DoublePlusValue : Value()
object DoubleMinusValue : Value()
object DoubleTimesValue : Value()
object StringPlusValue : Value()

val Value.function get() = (this as FunctionValue).function
val Value.string get() = (this as StringValue).string
val Value.double get() = (this as DoubleValue).double

fun value(string: String): Value = StringValue(string)
fun value(double: Double): Value = DoubleValue(double)
fun value(int: Int): Value = value(int.toDouble())

