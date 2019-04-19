package leo32.runtime

import leo.base.replace
import leo.binary.utf8ByteSeq

data class CoreString(
	val string: String)

val String.core
	get() =
		CoreString(this)

val CoreString.byteSeq
	get() =
		string
			.utf8ByteSeq
			.replace('.'.toByte() to 0.toByte())
