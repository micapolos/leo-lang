package leo.base

fun <R> Byte.foldBits(initial: R, fn: (R, Bit) -> R): R {
	val b8 = initial
	val b7 = fn(b8, toInt().and(0b10000000).bit)
	val b6 = fn(b7, toInt().and(0b01000000).bit)
	val b5 = fn(b6, toInt().and(0b00100000).bit)
	val b4 = fn(b5, toInt().and(0b00010000).bit)
	val b3 = fn(b4, toInt().and(0b00001000).bit)
	val b2 = fn(b3, toInt().and(0b00000100).bit)
	val b1 = fn(b2, toInt().and(0b00000010).bit)
	val b0 = fn(b1, toInt().and(0b00000001).bit)
	return b0
}

val Byte.bitStack: Stack<Bit>
	get() =
		foldBits(nullStack()) { bitStackOrNull, bit ->
			bitStackOrNull.push(bit)
		}!!
