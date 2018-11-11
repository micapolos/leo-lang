package leo

import java.io.InputStream
import java.io.OutputStream

fun evaluate(inputStream: InputStream, outputStream: OutputStream, errorStream: OutputStream) {
	var currentRepl = emptyRepl
  while (true) {
    val int = inputStream.read()

	  if (int == -1) {
		  val script = currentRepl.evaluatedScript
		  if (script == null) errorStream.write("leo: end of stream\n".toByteArray())
		  else script.term.coreString.toByteArray().let(outputStream::write)
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
