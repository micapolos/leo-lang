package leo32.c

import java.nio.ByteBuffer

val mem = newMem()

class Mem(
	var byteArray: ByteArray,
	var byteBuffer: ByteBuffer,
	var size: Int)

fun newMem(): Mem {
	val byteArray = ByteArray(65536)
	return Mem(byteArray, ByteBuffer.wrap(byteArray), 0)
}

fun Mem.grow(byteCount: Int): Int {
	val oldSize = size
	val newSize = size + byteCount
	var newByteArraySize = byteArray.size
	while (newByteArraySize < newSize) {
		newByteArraySize = newByteArraySize shl 1
		if (newByteArraySize < 0) error("out of memory")
	}
	if (byteArray.size != newByteArraySize) resize(newByteArraySize)
	return oldSize
}

fun Mem.resize(newSize: Int) {
	size = newSize
	byteArray = byteArray.copyOf(newSize)
	byteBuffer = ByteBuffer.wrap(byteArray)
}

fun Mem.byte(ptr: ptr) = byteArray[ptr]
fun Mem.int(ptr: ptr) = byteBuffer.getInt(ptr)

fun Mem.set(ptr: ptr, byte: Byte) {
	byteArray[ptr] = byte
}

fun Mem.set(ptr: ptr, int: Int) {
	byteBuffer.putInt(ptr, int)
}
