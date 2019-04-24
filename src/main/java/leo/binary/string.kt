package leo.binary

import leo.base.Seq
import leo.base.utf8ByteArray
import leo.base.utf8String

val String.utf8BitSeq
	get() =
		utf8ByteArray.bitSeq

val String.utf8ByteSeq
	get() =
		utf8ByteArray.seq

val Seq<Byte>.utf8String
	get() =
		byteArray.utf8String
