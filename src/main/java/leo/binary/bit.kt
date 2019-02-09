package leo.binary

sealed class Bit
object Bit0 : Bit()
object Bit1 : Bit()

val Bit.digitChar
	get() =
		when (this) {
			Bit0 -> '0'
			Bit1 -> '1'
		}

val Bit.int
	get() =
		when (this) {
			Bit0 -> 0
			Bit1 -> 1
		}

fun Appendable.appendDigit(bit: Bit): Appendable =
	append(bit.digitChar)