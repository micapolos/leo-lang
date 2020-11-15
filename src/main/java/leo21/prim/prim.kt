package leo21.prim

import leo14.Literal
import leo14.Number
import leo14.NumberLiteral
import leo14.ScriptLine
import leo14.Scriptable
import leo14.StringLiteral
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
				DoubleSinusPrim -> script("double" lineTo script(), "sinus" lineTo script())
				DoubleCosinusPrim -> script("double" lineTo script(), "cosinus" lineTo script())
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
object DoubleSinusPrim : Prim()
object DoubleCosinusPrim : Prim()
object StringPlusStringPrim : Prim()

val nilPrim: Prim = NilPrim
val Prim.string get() = (this as StringPrim).string
val Prim.double get() = (this as DoublePrim).double

val Double.prim get() = prim(this)
val String.prim get() = prim(this)
val Number.prim get() = prim(bigDecimal.toDouble())
val Literal.prim
	get() = when (this) {
		is StringLiteral -> string.prim
		is NumberLiteral -> number.prim
	}

fun prim(string: String): Prim = StringPrim(string)
fun prim(double: Double): Prim = DoublePrim(double)
fun prim(int: Int): Prim = prim(int.toDouble())
