package leo32

import leo.base.*
import leo.binary.Bit
import leo.binary.isZero
import kotlin.experimental.or

data class BitToByte(
	val byte: Byte,
	val bitMask: Byte)

val newBitToByte =
	BitToByte(0.clampedByte, 0x80.clampedByte)

fun BitToByte.invoke(bit: Bit) =
	BitToByte(
		if (bit.isZero) byte else byte.or(bitMask),
		bitMask.ushr1)

val BitToByte.byteIsReady
	get() =
		bitMask.int == 0

fun Writer<Byte>.invoke(bitToByte: BitToByte): Writer<Bit> =
	Writer { bit ->
		bitToByte.invoke(bit).let { nextBitToByte ->
			if (nextBitToByte.byteIsReady) write(nextBitToByte.byte).invoke(newBitToByte)
			else invoke(nextBitToByte)
		}
	}

val Writer<Byte>.bitToByte
	get() =
		invoke(newBitToByte)