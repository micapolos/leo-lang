package leo.java.io

import leo.base.EnumBit
import leo.base.Writer
import leo.base.byteBitWriter
import java.io.OutputStream

fun OutputStream.put(byte: Byte): OutputStream {
	write(byte.toInt())
	return this
}

val OutputStream.byteWriter: Writer<Byte>
	get() =
		Writer { byte -> put(byte).byteWriter }

val OutputStream.bitWriter: Writer<EnumBit>
	get() =
		byteWriter.byteBitWriter
