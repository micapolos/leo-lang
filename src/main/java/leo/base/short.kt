package leo.base

import leo.binary.Bit

val Short.byteStream: Stream<Byte>
	get() =
		toInt().shr(8).toByte()
			.onlyStream
			.then { toByte().onlyStream }

val Short.bitStream: Stream<Bit>
	get() =
		byteStream.map(Byte::bitStream).join