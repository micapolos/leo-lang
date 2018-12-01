package leo.base

data class BitCount(
	val highestBitIndexBinaryOrNull: Binary?) {
	override fun toString() = "bit count $intOrNull"
}

val Binary?.orNullHighestBitIndexBitCount: BitCount
	get() =
		BitCount(this)

val BitCount?.increment: BitCount
	get() =
		if (this == null) BitCount(null)
		else highestBitIndexBinaryOrNull.incrementAndGrow.orNullHighestBitIndexBitCount

val BitCount.intOrNull: Int?
	get() =
		highestBitIndexBinaryOrNull.incrementAndGrow.intOrNull ?: 1

val bitBitCount = nullOf<Binary>().orNullHighestBitIndexBitCount
val byteBitCount = binary(1, 1, 1).orNullHighestBitIndexBitCount
val shortBitCount = binary(1, 1, 1, 1).orNullHighestBitIndexBitCount
val intBitCount = binary(1, 1, 1, 1, 1).orNullHighestBitIndexBitCount
val longBitCount = binary(1, 1, 1, 1, 1, 1).orNullHighestBitIndexBitCount
val floatBitCount = intBitCount
val doubleBitCount = longBitCount
