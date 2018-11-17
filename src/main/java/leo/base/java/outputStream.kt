package leo.base.java

import leo.base.Writer
import java.io.OutputStream

fun OutputStream.put(byte: Byte): OutputStream {
	write(byte.toInt())
	return this
}

val OutputStream.byteWriter: Writer<Byte, OutputStream>
	get() =
		Writer(this, OutputStream::put)
