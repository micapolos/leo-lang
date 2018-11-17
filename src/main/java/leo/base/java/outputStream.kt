package leo.base.java

import leo.base.Writer
import java.io.OutputStream

fun OutputStream.put(byte: Byte): OutputStream {
	write(byte.toInt())
	return this
}

val OutputStream.byteWriter: Writer<Byte>
	get() =
		Writer { byte -> put(byte).byteWriter }
