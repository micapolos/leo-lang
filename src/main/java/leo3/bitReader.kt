package leo3

import leo.base.*
import leo.binary.Bit

data class BitReader(
	val byteReader: ByteReader,
	val partialByte: PartialByte)

val ByteReader.completedBitReader
	get() = BitReader(this, empty.partialByte)

fun BitReader.read(bit: Bit): BitReader? {
	val nextPartialByte = partialByte.plus(bit)
	return nextPartialByte.fullByteOrNull.ifNotNullOr(
		{ completeByte -> byteReader.read(completeByte)?.completedBitReader },
		{ copy(partialByte = nextPartialByte) })
}

val BitReader.isCompleted
	get() = partialByte.isEmpty

val BitReader.completedByteReaderOrNull
	get() = notNullIf(isCompleted) { byteReader }

val BitReader.bitSeq: Seq<Bit>
	get() = flatSeq(byteReader.bitSeq, partialByte.bitSeq)

val Value.completedBitReader
	get() =
		this
			.lineReader
			.completedTokenReader
			.completedWordReader
			.completedByteReader
			.completedBitReader

val BitReader.valueOrNull
	get() =
		orNull
			?.completedByteReaderOrNull
			?.completedWordReader
			?.tokenReader
			?.lineReader
			?.value
