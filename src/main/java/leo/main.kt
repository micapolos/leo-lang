package leo

import java.io.ByteArrayInputStream

fun main(args: Array<String>) {
	emptyRepl
		.evaluate(System.`in`, System.err)
		?.evaluate(ByteArrayInputStream(args.joinToString(" ").toByteArray()), System.err)
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
