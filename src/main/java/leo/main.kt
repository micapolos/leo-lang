package leo

import java.io.ByteArrayInputStream
import java.io.InputStream

fun main(args: Array<String>) {
	emptyRepl
		.evaluate(args.inputStream, System.err)
		?.let { replOrNull ->
			Unit.foldBytes(replOrNull) { byte ->
				System.out.write(byte)
				System.out.flush()
			}
		}
	if (!args.isEmpty()) System.out.println()
}

val Array<String>.inputStream: InputStream
	get() =
		if (isEmpty()) System.`in`
		else ByteArrayInputStream(joinToString(" ").toByteArray())
