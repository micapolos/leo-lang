package leo.lab

import leo.base.*
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
	System.`in`.bitStreamOrNull?.then {
		ByteArrayInputStream(args.joinToString(" ").toByteArray()).bitStreamOrNull
	}