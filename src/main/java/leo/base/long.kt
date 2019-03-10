package leo.base

val Long.intStream: Stream<Int>
	get() =
		shr(32).toInt().onlyStream
			.then { toInt().onlyStream }

val Long.bitStream: Stream<EnumBit>
	get() =
		intStream.map(Int::bitStream).join
