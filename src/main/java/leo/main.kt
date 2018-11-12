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

	// TODO(micapolos): This is not correct in general, but is convenient until we implement Printer.
	// TODO(micapolos): Move it to Printer when implemented.
	System.out.println()
}

val Array<String>.inputStream: InputStream
	get() =
		if (isEmpty()) System.`in`
		else ByteArrayInputStream(joinToString(" ").toByteArray())
