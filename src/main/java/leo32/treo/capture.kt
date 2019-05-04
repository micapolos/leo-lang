package leo32.treo

import leo.binary.Bit

data class Capture(val variable: Variable)

fun capture(variable: Variable) = Capture(variable)

fun Capture.enter(bit: Bit) {
	variable.bit = bit
}

val Capture.charSeq
	get() =
		variable.charSeq