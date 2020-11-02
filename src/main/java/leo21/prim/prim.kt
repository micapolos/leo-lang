package leo21.prim

sealed class Prim
data class StringPrim(val string: String) : Prim()
data class DoublePrim(val double: Double) : Prim()

object DoublePlusDoublePrim : Prim()
object DoubleMinusDoublePrim : Prim()
object DoubleTimesDoublePrim : Prim()
object StringPlusStringPrim : Prim()

data class PlusDoublePrim(val double: Double) : Prim()
data class MinusDoublePrim(val double: Double) : Prim()
data class TimesDoublePrim(val double: Double) : Prim()
data class PlusStringPrim(val string: String) : Prim()

val Prim.string get() = (this as StringPrim).string
val Prim.double get() = (this as DoublePrim).double

fun prim(string: String): Prim = StringPrim(string)
fun prim(double: Double): Prim = DoublePrim(double)
fun prim(int: Int): Prim = prim(int.toDouble())
