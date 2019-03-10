package leo32

import leo.binary.utf8BitSeq

data class Id(
	val string: String)

val String.id
	get() =
		Id(this)

val Id.codeString
	get() =
		if (string.isEmpty()) "empty\u0000"
		else string.replace("\\", "\\\\").replace("\u0000", "\\0") + '\u0000'

val Id.codeBitSeq
	get() =
		codeString.utf8BitSeq