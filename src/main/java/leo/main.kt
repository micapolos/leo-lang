package leo

import leo.base.Bit
import leo.base.Stream
import leo.base.java.bitStreamOrNull
import leo.base.java.bitWriter
import leo.base.orNullThen
import java.io.ByteArrayInputStream

fun main(args: Array<String>) {
	runRepl(
		inputBitStreamOrNull(args),
		System.out.bitWriter,
		System.err.bitWriter)

	System.out.flush()
	System.err.flush()
}

fun inputBitStreamOrNull(args: Array<String>): Stream<Bit>? =
	System.`in`.bitStreamOrNull.orNullThen {
		ByteArrayInputStream(args.joinToString(" ").toByteArray()).bitStreamOrNull
	}