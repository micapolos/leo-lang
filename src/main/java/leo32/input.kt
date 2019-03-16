package leo32

import leo.binary.Bit
import leo.binary.Stack32
import leo.binary.emptyStack32

data class Input(
	val bitStack32: Stack32<Bit>)

val Stack32<Bit>.input
	get() =
		Input(this)

val emptyInput =
	emptyStack32<Bit>().input
