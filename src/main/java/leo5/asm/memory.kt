package leo5.asm

import java.nio.ByteBuffer

const val pageSize = 65536

class Memory(var byteBuffer: ByteBuffer)

val newMemory get() = Memory(ByteBuffer.allocate(pageSize))
val Memory.size get() = byteBuffer.capacity()
fun Memory.grow(pageCount: Int) {
	TODO()
}

fun Memory.int(index: Int) = byteBuffer.getInt(index)
fun Memory.put(index: Int, int: Int) {
	byteBuffer.putInt(index, int)
}

inline fun Memory.intOp1(index: Int, fn: Int.() -> Int) {
	put(index, int(index).fn())
}

inline fun Memory.intOp2(index: Int, argIndex: Int, fn: Int.(Int) -> Int) {
	put(index, int(index).fn(int(argIndex)))
}
