package leo

import leo.base.orNull
import java.io.InputStream
import java.io.OutputStream

fun evaluate(inputStream: InputStream, outputStream: OutputStream, errorStream: OutputStream) {
  var evaluatorOrNull = evaluator.orNull
  while (true) {
    val int = inputStream.read()
    if (int == -1) break
    if (evaluatorOrNull != null) {
      evaluatorOrNull = evaluatorOrNull.push(int.toByte())
      if (evaluatorOrNull == null) errorStream.write("leo: parse error".toByteArray())
    }
  }

  if (evaluatorOrNull == null) {
    errorStream.write("leo: end of stream".toByteArray())
  } else {
    evaluatorOrNull
      .evaluatedScript
      ?.term
      ?.coreString
      ?.toByteArray()
      ?.let(outputStream::write)
  }
}
