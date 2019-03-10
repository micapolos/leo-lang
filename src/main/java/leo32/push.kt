package leo32

import leo.binary.Bit

data class Push(
	val bit: Bit)

val Bit.push
	get() =
		Push(this)