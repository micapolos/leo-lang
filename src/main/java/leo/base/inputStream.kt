package leo.base

import java.io.InputStream

val InputStream.byteStreamOrNull: Stream<Byte>?
	get() =
		read().let { readInt ->
			if (readInt == -1) null
			else readInt.toByte().onlyStream
		}

val InputStream.bitStreamOrNull: Stream<Bit>?
	get() =
		byteStreamOrNull?.map(Byte::bitStream)?.join
