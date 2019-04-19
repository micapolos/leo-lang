package leo32.vm

import java.nio.ByteBuffer
import java.nio.ByteBuffer.allocateDirect

class Mem(
	val byteBuffer: ByteBuffer)

val ByteBuffer.mem
	get() =
		Mem(this)

fun mem(size: Int) =
	allocateDirect(size)

fun Mem.set(index: Int, int: Int) {
	byteBuffer.putInt(index, int)
}

fun Mem.int(index: Int) =
	byteBuffer.getInt(index)
