package leo.base

inline val Int.clampedByte
	get() =
		toByte()

inline val Int.clampedShort
	get() =
		toShort()

val Int.shortStream: Stream<Short>
	get() =
		shr(16).toShort().onlyStream
			.then { toShort().onlyStream }

val Int.bitStream: Stream<EnumBit>
	get() =
		shortStream.map(Short::bitStream).join

val Int.indexSize: Int
	get() =
		32 - Integer.numberOfLeadingZeros(this - 1)

val Int.bitCount: Int
	get() =
		indexSize

val intBitCountInt = 32
val intHsbMaskInt = 1.shl(intBitCountInt - 1)

val Int.bitMaskOrNull: Int?
	get() =
		when {
			this in 0..31 -> 1.shl(this) - 1
			this == 32 -> -1
			else -> null
		}

val Int.nearestPot: Int
	get() =
		if (this == 0) 0
		else 1.shl(bitCount)

val Int.pow2: Int
	get() =
		when {
			this < 0 || this >= 32 -> 0
			else -> 1.shl(this)
		}

fun Int.bitSeq(count: Int): Seq<EnumBit> =
	Seq {
		if (count == 0) null
		else and((count - 1).pow2).enumBit then bitSeq(count - 1)
	}

fun Int.littleEndianBitSeq(bitCount: Int): Seq<EnumBit> =
	Seq {
		if (bitCount == 0) null
		else and(1).enumBit.then(ushr(1).littleEndianBitSeq(bitCount.dec()))
	}

val maxInt = Integer.MAX_VALUE

val Int.incOrNull
	get() =
		if (this == Int.MAX_VALUE) null else this + 1

val Int.decOrNull
	get() =
		if (this == Int.MIN_VALUE) null else this - 1

val Int.uincOrNull
	get() =
		if (this == -1) null else this + 1

val Int.udecOrNull
	get() =
		if (this == 0) null else this - 1
