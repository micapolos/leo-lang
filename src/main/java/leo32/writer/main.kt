package leo32.writer

import leo.base.empty
import leo.base.fold
import leo.java.io.byteSeq
import leo.java.lang.sttyPrivateMode
import leo32.leoReader

fun main() {
	sttyPrivateMode()

	systemByteSeqWriter
		.leoWriter(empty.leoReader)
		.fold(System.`in`.byteSeq) { write(it) }
}