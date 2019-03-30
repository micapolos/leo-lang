package leo32

import leo.base.clampedByte
import leo.base.int
import leo.base.ushr1
import leo.binary.Bit
import leo.binary.isZero
import kotlin.experimental.or

data class BitToByte(
	val byte: Byte,
	val bitMask: Byte)

val initialBitToByte =
	BitToByte(0.clampedByte, 0x80.clampedByte)

fun BitToByte.invoke(bit: Bit) =
	BitToByte(
		if (bit.isZero) byte else byte.or(bitMask),
		bitMask.ushr1)

val BitToByte.byteIsReady
	get() =
		bitMask.int == 0
