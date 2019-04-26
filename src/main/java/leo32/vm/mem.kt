package leo32.vm

import java.nio.ByteBuffer

var memByteArray: ByteArray = ByteArray(65536)
var memByteBuffer: ByteBuffer = ByteBuffer.wrap(memByteArray)
var memSize = 0

fun memAlloc(byteCount: Size): Ptr {
	val ptr = memSize
	memSize += byteCount
	while (memByteArray.size < byteCount) {
		memByteArray = memByteArray.copyOf(memByteArray.size shl 1)
		memByteBuffer = ByteBuffer.wrap(memByteArray)
	}
	return ptr
}
