package leo3

import leo.base.*
import leo.binary.Bit
import leo.binary.isZero
import kotlin.experimental.and
import kotlin.experimental.or

data class PartialByte(
	val byte: Byte,
	val mask: Byte)

val Empty.partialByteMask
	get() = byte(0b10000000)

val Empty.partialByte
	get() = PartialByte(byte(0), partialByteMask)

val PartialByte.isEmpty
	get() = mask == empty.partialByteMask

val PartialByte.isFull
	get() = mask == byte(0)

val PartialByte.fullByteOrNull
	get() = notNullIf(isFull) { byte }

fun PartialByte.plus(bit: Bit): PartialByte {
	val nextByte = byte.or(if (bit.isZero) 0 else mask)
	val nextMask = mask.ushr1
	return copy(byte = nextByte, mask = nextMask)
}

fun PartialByte.bitSeqFrom(startMask: Byte): Seq<Bit> =
	Seq {
		notNullIf(startMask != mask) {
			byte.and(mask).bit.then(bitSeqFrom(mask.ushr1))
		}
	}

val PartialByte.bitSeq
	get() = bitSeqFrom(empty.partialByteMask)