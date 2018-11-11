package leo

import java.io.ByteArrayInputStream
import java.io.InputStream

fun main(args: Array<String>) {
	evaluate(
		args.inputStream,
		System.out,
		System.err)
	if (!args.isEmpty()) System.out.println()
}

val Array<String>.inputStream: InputStream
	get() =
		if (isEmpty()) System.`in`
		else ByteArrayInputStream(joinToString(" ").toByteArray())
