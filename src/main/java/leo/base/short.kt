package leo.base

val Short.byteStream: Stream<Byte>
	get() =
		toInt().shr(8).toByte()
			.onlyStream
			.then { toByte().onlyStream }

val Short.bitStream: Stream<EnumBit>
	get() =
		byteStream.map(Byte::bitStream).join