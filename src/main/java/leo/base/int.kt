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
