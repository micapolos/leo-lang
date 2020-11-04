package leo21.prim

import leo14.ScriptLine
import leo14.Scriptable
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.script

sealed class Prim : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() =
			"prim" lineTo when (this) {
				NilPrim -> script("nil")
				is StringPrim -> script(literal(string))
				is DoublePrim -> script(literal(double))
				DoublePlusDoublePrim -> script("double" lineTo script(), "plus" lineTo script("double"))
				DoubleMinusDoublePrim -> script("double" lineTo script(), "minus" lineTo script("double"))
				DoubleTimesDoublePrim -> script("double" lineTo script(), "times" lineTo script("double"))
				StringPlusStringPrim -> script("string" lineTo script(), "plus" lineTo script("string"))
				is PlusDoublePrim -> script(line(literal(double)), "plus" lineTo script("double"))
				is MinusDoublePrim -> script(line(literal(double)), "minus" lineTo script("double"))
				is TimesDoublePrim -> script(line(literal(double)), "times" lineTo script("double"))
				is PlusStringPrim -> script(line(literal(string)), "plus" lineTo script("double"))
			}
}

object NilPrim : Prim()

data class StringPrim(val string: String) : Prim() {
	override fun toString() = super.toString()
}

data class DoublePrim(val double: Double) : Prim() {
	override fun toString() = super.toString()
}

object DoublePlusDoublePrim : Prim()
object DoubleMinusDoublePrim : Prim()
object DoubleTimesDoublePrim : Prim()
object StringPlusStringPrim : Prim()

data class PlusDoublePrim(val double: Double) : Prim() {
	override fun toString() = super.toString()
}

data class MinusDoublePrim(val double: Double) : Prim() {
	override fun toString() = super.toString()
}

data class TimesDoublePrim(val double: Double) : Prim() {
	override fun toString() = super.toString()
}

data class PlusStringPrim(val string: String) : Prim() {
	override fun toString() = super.toString()
}

val nilPrim: Prim = NilPrim
val Prim.string get() = (this as StringPrim).string
val Prim.double get() = (this as DoublePrim).double

val Double.prim get() = prim(this)
val String.prim get() = prim(this)

fun prim(string: String): Prim = StringPrim(string)
fun prim(double: Double): Prim = DoublePrim(double)
fun prim(int: Int): Prim = prim(int.toDouble())
