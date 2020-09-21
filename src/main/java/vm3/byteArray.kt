package vm3

import leo.base.byte0
import leo.base.byte1
import leo.base.byte2
import leo.base.byte3
import leo.base.int

inline fun ByteArray.byte(index: Int) = this[index]

inline fun ByteArray.int(index: Int) = int(
	byte(index + 3),
	byte(index + 2),
	byte(index + 1),
	byte(index))

inline fun ByteArray.set(index: Int, byte: Byte) {
	this[index] = byte
}

inline fun ByteArray.set(index: Int, int: Int) {
	set(index + 0, int.byte0)
	set(index + 1, int.byte1)
	set(index + 2, int.byte2)
	set(index + 3, int.byte3)
}

inline fun ByteArray.updateInt(index: Int, fn: (Int) -> Int) {
	set(index, fn(int(index)))
}