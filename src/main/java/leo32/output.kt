package leo32

import leo.binary.Bit
import leo.binary.Stack32
import leo.binary.emptyStack32

data class Output(
	val bitStack32: Stack32<Bit>)

val Stack32<Bit>.output
	get() =
		Output(this)

val emptyOutput =
	emptyStack32<Bit>().output
