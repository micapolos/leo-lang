package leo32

import leo.base.*

fun leo(byteSeq: Seq<Byte>, fn: Seq<Byte>.() -> Unit) =
	empty.leoReader.orNull.fold(byteSeq) { byte ->
		ifNotNull { leoReader ->
			val nextLeoReader = leoReader.plus(byte)
			nextLeoReader?.byteSeq?.fn()
			nextLeoReader
		}
	}
