package leo.base

import leo.binary.Bit

val Long.intStream: Stream<Int>
	get() =
		shr(32).toInt().onlyStream
			.then { toInt().onlyStream }

val Long.bitStream: Stream<Bit>
	get() =
		intStream.map(Int::bitStream).join
