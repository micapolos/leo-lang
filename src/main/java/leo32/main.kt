package leo32

import leo.base.empty
import leo.base.fold
import leo.base.orNull
import leo.java.io.byteSeq

fun main() {
	empty
		.leoReader
		.orNull
		.fold(System.`in`.byteSeq) { byte -> this?.plus(byte) }
		?.termOrNull
		?.let { term -> System.out.println(term.script.code) }
		?: System.err.println("syntax error")

	System.out.flush()
	System.err.flush()
}
