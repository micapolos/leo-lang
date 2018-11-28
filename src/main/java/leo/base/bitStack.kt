package leo.base

data class BitStack(
	val bitArray: BitArray,
	val topIndexOrNull: Binary?)

val BitArray.bitStack: BitStack
	get() =
		BitStack(this, depth.sizeZeroBinaryOrNull)
