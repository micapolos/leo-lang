package leo.base

data class BitCount(
	val int: Int)

val Int.bitCount: BitCount
	get() =
		BitCount(this)

val byteBitCount = 8.bitCount
val shortBitCount = 16.bitCount
val intBitCount = 32.bitCount
val longBitCount = 64.bitCount
val floatBitCount = 32.bitCount
val doubleBitCount = 64.bitCount
