package leo

import java.io.InputStream
import java.io.OutputStream

fun Repl.evaluate(inputStream: InputStream, errorStream: OutputStream): Repl? {
	var currentRepl = this

	while (true) {
		val int = inputStream.read()
		if (int == -1) break
		val repl = currentRepl.push(int.toByte())
		if (repl != null) currentRepl = repl
		else {
			errorStream.write("leo: parse error\n".toByteArray())
			break
		}
	}

	return currentRepl
}

fun OutputStream.write(byte: Byte) =
	write(byte.toInt())