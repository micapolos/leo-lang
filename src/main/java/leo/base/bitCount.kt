package leo.base

data class BitCount(
	val int: Int)

val Int.bitCountOrNull: BitCount?
	get() =
		if (this <= 0) null
		else BitCount(this)

val BitCount.increment: BitCount?
	get() =
		int.inc().bitCountOrNull

val bitBitCount = 1.bitCountOrNull!!
val byteBitCount = 8.bitCountOrNull!!
val shortBitCount = 16.bitCountOrNull!!
val intBitCount = 32.bitCountOrNull!!
val longBitCount = 64.bitCountOrNull!!
val floatBitCount = intBitCount
val doubleBitCount = longBitCount
