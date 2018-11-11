package leo

import java.io.InputStream
import java.io.OutputStream

fun evaluate(inputStream: InputStream, outputStream: OutputStream, errorStream: OutputStream) {
	var currentRepl = emptyRepl
	while (true) {
		val int = inputStream.read()

		if (int == -1) {
			Unit.foldBytes(currentRepl) { byte ->
				outputStream.write(byte.toInt())
			}
			break
		}

		val repl = currentRepl.push(int.toByte())
		if (repl == null) {
			errorStream.write("leo: parse error\n".toByteArray())
			break
		} else {
			currentRepl = repl
		}
	}
}
