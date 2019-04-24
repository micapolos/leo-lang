package leo32.writer

import leo.base.empty
import leo.base.fold
import leo.java.io.byteSeq
import leo32.runtime.leoReader

fun main() {
	try {
		Runtime.getRuntime().exec(arrayOf("sh", "-c", "stty -icanon min 1 < /dev/tty"))
		Runtime.getRuntime().exec(arrayOf("sh", "-c", "stty -echo < /dev/tty"))
		systemByteSeqWriter
			.leoWriter(empty.leoReader)
			.fold(System.`in`.byteSeq) { write(it) }
	} finally {
		Runtime.getRuntime().exec(arrayOf("sh", "-c", "stty sane < /dev/tty"))
	}
}