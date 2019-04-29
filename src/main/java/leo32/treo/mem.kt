package leo32.treo

import java.nio.ByteBuffer

class Mem(
	var byteArray: ByteArray,
	var byteBuffer: ByteBuffer,
	var size: Int)

fun newMem(): Mem {
	val byteArray = ByteArray(65536)
	return Mem(byteArray, ByteBuffer.wrap(byteArray), 0)
}

fun Mem.alloc(byteCount: Int): Int {
	val oldSize = size
	size += byteCount
	while (byteArray.size < byteCount) {
		byteArray = byteArray.copyOf(byteArray.size shl 1)
		byteBuffer = ByteBuffer.wrap(byteArray)
	}
	return oldSize
}
