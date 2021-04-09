package leo.base.io

import java.io.File

data class Writer(private val javaWriter: java.io.Writer) {
	fun writeIO(string: String): IO<Unit> = IO.unsafe { javaWriter.write(string) }
}

fun File.writeIO(fn: (Writer) -> IO<Unit>): IO<Unit> =
	IO.unsafe { outputStream().writer().use { fn(Writer(it)).unsafeValue } }

fun main() {
	File("/Users/micapolos/out.txt")
		.writeIO { writer ->
			io
				.then { "Writing...".printlnIO }
				.then { writer.writeIO("jajko\n") }
				.then { writer.writeIO("kura\n") }
				.then { writer.writeIO("osioł\n") }
				.then { "Written...".printlnIO }
		}
		.unsafeRun
}