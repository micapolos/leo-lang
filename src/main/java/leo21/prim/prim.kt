package leo21.prim

import leo14.Literal
import leo14.Number
import leo14.NumberLiteral
import leo14.ScriptLine
import leo14.Scriptable
import leo14.StringLiteral
import leo14.lineTo
import leo14.literal
import leo14.number
import leo14.script

sealed class Prim : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() =
			"prim" lineTo when (this) {
				NilPrim -> script("nil")
				is StringPrim -> script(literal(string))
				is NumberPrim -> script(literal(number))
				NumberPlusNumberPrim -> script("number" lineTo script(), "plus" lineTo script("number"))
				NumberMinusNumberPrim -> script("number" lineTo script(), "minus" lineTo script("number"))
				NumberTimesNumberPrim -> script("number" lineTo script(), "times" lineTo script("number"))
				NumberEqualsNumberPrim -> script("number" lineTo script(), "equals" lineTo script("number"))
				StringPlusStringPrim -> script("string" lineTo script(), "plus" lineTo script("string"))
				NumberStringPrim -> script("number" lineTo script(), "text" lineTo script())
				NumberSinusPrim -> script("number" lineTo script(), "sinus" lineTo script())
				NumberCosinusPrim -> script("number" lineTo script(), "cosinus" lineTo script())
				StringLengthPrim -> script("string" lineTo script(), "length" lineTo script())
				StringTryNumberPrim -> script("string" lineTo script(), "try" lineTo script("number" lineTo script()))
				StringEqualsStringPrim -> script("string" lineTo script(), "equals" lineTo script("string"))
			}
}

object NilPrim : Prim()

data class StringPrim(val string: String) : Prim() {
	override fun toString() = super.toString()
}

data class NumberPrim(val number: Number) : Prim() {
	override fun toString() = super.toString()
}

object NumberPlusNumberPrim : Prim()
object NumberMinusNumberPrim : Prim()
object NumberTimesNumberPrim : Prim()
object NumberEqualsNumberPrim : Prim()
object NumberStringPrim : Prim()
object NumberSinusPrim : Prim()
object NumberCosinusPrim : Prim()
object StringPlusStringPrim : Prim()
object StringEqualsStringPrim : Prim()
object StringLengthPrim : Prim()
object StringTryNumberPrim : Prim()

val nilPrim: Prim = NilPrim
val Prim.string get() = (this as StringPrim).string
val Prim.number get() = (this as NumberPrim).number

val String.prim get() = prim(this)
val Number.prim get() = prim(this)
val Double.prim get() = prim(this)
val Literal.prim
	get() = when (this) {
		is StringLiteral -> string.prim
		is NumberLiteral -> number.prim
	}

fun prim(string: String): Prim = StringPrim(string)
fun prim(number: Number): Prim = NumberPrim(number)
fun prim(double: Double): Prim = prim(double.number)
fun prim(int: Int): Prim = prim(int.number)

fun String.isEqualTo(string: String): Boolean = this == string
