package leo.lab

import leo.base.Bit
import leo.base.Stream

data class BitReader(
	val byteReader: ByteReader,
	val byteInt: Int,
	val maskInt: Int)

val emptyBitReader =
	BitReader(emptyByteReader, 0, 0x80)

fun BitReader.read(bit: Bit): BitReader? {
	val nextByteInt = byteInt.or(if (bit == Bit.ONE) maskInt else 0)
	val nextMaskInt = maskInt.shr(1)
	return if (nextMaskInt == 0) {
		byteReader.read(nextByteInt.toByte())?.let { nextByteReader ->
			BitReader(nextByteReader, 0, 0x80)
		}
	} else {
		BitReader(byteReader, nextByteInt, nextMaskInt)
	}
}

val BitReader.byteStreamOrNull: Stream<Byte>?
	get() =
		byteReader.byteStreamOrNull
