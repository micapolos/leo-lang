package leo.base

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

fun Int.bits(count: UInt): Iterable<Bit> =
	object : Iterable<Bit> {
		var bit = count.signed
		override fun iterator() =
			iterator {
				if (bit == 0) null else and((--bit).pow2).bit
			}
	}

val maxInt = Integer.MAX_VALUE
