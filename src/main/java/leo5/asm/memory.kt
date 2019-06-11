package leo5.asm

import java.nio.ByteBuffer

class Memory(var byteBuffer: ByteBuffer)

fun memory(size: Size) = Memory(ByteBuffer.allocate(size.int))

fun Memory.int(ptr: Ptr) = byteBuffer.getInt(ptr.int)

fun Memory.set(ptr: Ptr, int: Int) {
	byteBuffer.putInt(ptr.int, int)
}

inline fun Memory.intOp1(ptr: Ptr, fn: Int.() -> Int) {
	set(ptr, int(ptr).fn())
}

inline fun Memory.intOp2(lhs: Ptr, rhs: Ptr, fn: Int.(Int) -> Int) {
	set(lhs, int(lhs).fn(int(rhs)))
}
