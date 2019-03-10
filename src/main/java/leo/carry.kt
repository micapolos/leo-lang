package leo

import leo.base.EnumBit

data class Carry(
	val bit: EnumBit)

val EnumBit.carry
	get() =
		Carry(this)
