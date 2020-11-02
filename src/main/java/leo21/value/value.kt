package leo21.value

sealed class Value
data class StringValue(val string: String) : Value()
data class DoubleValue(val double: Double) : Value()

object DoublePlusDoubleValue : Value()
object DoubleMinusDoubleValue : Value()
object DoubleTimesDoubleValue : Value()
object StringPlusStringValue : Value()

data class PlusDoubleValue(val double: Double) : Value()
data class MinusDoubleValue(val double: Double) : Value()
data class TimesDoubleValue(val double: Double) : Value()
data class PlusStringValue(val string: String) : Value()

val Value.string get() = (this as StringValue).string
val Value.double get() = (this as DoubleValue).double

fun value(string: String): Value = StringValue(string)
fun value(double: Double): Value = DoubleValue(double)
fun value(int: Int): Value = value(int.toDouble())
