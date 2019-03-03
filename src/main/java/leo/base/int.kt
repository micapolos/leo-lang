package leo.base

import leo.binary.Bit
import leo.binary.bit

inline val Int.clampedByte
	get() =
		toByte()

val Int.shortStream: Stream<Short>
	get() =
		shr(16).toShort().onlyStream
			.then { toShort().onlyStream }

val Int.bitStream: Stream<Bit>
	get() =
		shortStream.map(Short::bitStream).join

val Int.indexSize: Int
	get() =
		32 - Integer.numberOfLeadingZeros(this - 1)

val Int.bitCount: Int
	get() =
		indexSize

val Int.bitMaskOrNull: Int?
	get() =
		when {
			this in 0..31 -> 1.shl(this) - 1
			this == 32 -> -1
			else -> null
		}

val Int.pow2: Int
	get() =
		when {
			this < 0 || this >= 32 -> 0
			else -> 1.shl(this)
		}

fun Int.bitSequence(count: Int): Sequence<Bit> =
	Sequence {
		if (count == 0) null
		else and((count - 1).pow2).bit.thenNonEmptySequence(bitSequence(count - 1))
	}


val maxInt = Integer.MAX_VALUE
