package leo.base

data class BitStack(
	val bitArray: BitArray,
	val topIndexOrNull: Binary?)

val BitArray.fullBitStack: BitStack
	get() =
		BitStack(this, depth.sizeStackOrNullOf(Bit.ONE)?.reverseBinary)

//fun BitStack.push(bitArray: BitArray): BitStack =
//	growIfNeeded.run {
//		if (topIndexOrNull == null) BitStack(
//			bitArray.set(bit),
//			bitArray.depth.sizeZeroBinaryOrNull?.increment!!)
//		else BitStack(
//			bitArray.set(topIndexOrNull, bitArray)!!,
//			topIndexOrNull.increment)
//	}
//
//fun BitStack.indexPorPush(bitArray: BitArray): Binary =
//	if (topIndexOrNull == null)
//	  if (topIndexOrNull == null)
//		  BitStack(bitArray.incrementDepth, binary(1.bit))
//	  topIndexOrNull?.let { topIndex ->
//		  if (bitArray.depth < topIndex.bitCountInt) copy(bitArray = bitArray.grow).growIfNeeded
//		  else this
//	  } ?: this
