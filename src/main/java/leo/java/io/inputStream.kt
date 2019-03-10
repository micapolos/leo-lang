package leo.java.io

import leo.base.*
import java.io.InputStream

val InputStream.byteStreamOrNull: Stream<Byte>?
	get() =
		read().let { readInt ->
			if (readInt == -1) null
			else readInt.clampedByte.onlyStream.then { byteStreamOrNull }
		}

val InputStream.bitStreamOrNull: Stream<EnumBit>?
	get() =
		byteStreamOrNull?.map(Byte::bitStream)?.join
