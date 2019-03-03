package leo

import leo.binary.Bit

data class Carry(
	val bit: Bit)

val Bit.carry
	get() =
		Carry(this)
