package leo.binary

import leo.base.utf8ByteArray

val String.utf8BitSeq
	get() =
		utf8ByteArray.bitSeq

val String.utf8ByteSeq
	get() =
		utf8ByteArray.seq
