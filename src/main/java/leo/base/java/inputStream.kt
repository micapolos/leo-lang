package leo.base.java

import leo.base.*
import java.io.InputStream

val InputStream.byteStreamOrNull: Stream<Byte>?
	get() =
		read().let { readInt ->
			if (readInt == -1) null
			else readInt.byte.onlyStream.then { byteStreamOrNull }
		}

val InputStream.bitStreamOrNull: Stream<Bit>?
	get() =
		byteStreamOrNull?.map(Byte::bitStream)?.join
