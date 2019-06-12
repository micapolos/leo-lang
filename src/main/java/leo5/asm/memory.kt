package leo5.asm

import java.nio.ByteBuffer

class Memory(var byteBuffer: ByteBuffer)

fun memory(size: Size) = Memory(ByteBuffer.allocate(size.int))

fun Memory.getInt(address: Int) = byteBuffer.getInt(address)

fun Memory.set(address: Int, int: Int) = byteBuffer.putInt(address, int)

inline fun Memory.intOp1(lhs: Int, fn: Int.() -> Int) {
	set(lhs, getInt(lhs).fn())
}

inline fun Memory.intOp2(lhs: Int, rhs: Int, fn: Int.(Int) -> Int) {
	set(lhs, getInt(lhs).fn(getInt(rhs)))
}

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
