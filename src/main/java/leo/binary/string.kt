package leo.binary

import leo.base.utf8ByteArray

val String.utf8BitSeq
	get() =
		utf8ByteArray.bitSeq