package vm3

import leo.base.int
import java.io.InputStream

fun InputStream.readByte(): Byte {
	val int = read()
	return if (int == -1) throw RuntimeException("closed")
	else int.toByte()
}

fun InputStream.readInt(): Int {
	val byte0 = readByte()
	val byte1 = readByte()
	val byte2 = readByte()
	val byte3 = readByte()
	return int(byte3, byte2, byte1, byte0)
}