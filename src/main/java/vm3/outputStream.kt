package vm3

import leo.base.byte0
import leo.base.byte1
import leo.base.byte2
import leo.base.byte3
import java.io.ByteArrayOutputStream
import java.io.OutputStream

inline fun OutputStream.writeByte(byte: Byte) {
	write(byte.toInt())
}

inline fun OutputStream.writeOp(int: Int) {
	write(int)
}

inline fun OutputStream.writeInt(int: Int) {
	writeByte(int.byte0)
	writeByte(int.byte1)
	writeByte(int.byte2)
	writeByte(int.byte3)
}

inline fun ByteArrayOutputStream.writeIntHole(): Int =
	writeHole(4)

inline fun ByteArrayOutputStream.writeHole(size: Int): Int =
	size().also { repeat(size) { writeByte(0) } }
