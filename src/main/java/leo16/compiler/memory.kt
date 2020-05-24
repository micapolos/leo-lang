package leo16.compiler

import leo.base.byte0
import leo.base.byte1
import leo.base.byte2
import leo.base.byte3
import leo.base.map
import leo.base.stack
import leo.binary.seq
import leo13.Index
import leo16.invoke
import leo16.names.*
import leo16.value

data class Memory(val byteArray: ByteArray) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Memory

		if (!byteArray.contentEquals(other.byteArray)) return false

		return true
	}

	override fun hashCode(): Int {
		return byteArray.contentHashCode()
	}

	override fun toString() = asField.toString()
}

val ByteArray.memory get() = Memory(this)
val Int.sizeMemory get() = ByteArray(this).memory

val Memory.size get() = byteArray.size

inline fun Memory.set(index: Int, byte: Byte) {
	byteArray[index] = byte
}

fun Memory.set(index: Int, int: Int) {
	var index = index
	set(index++, int.byte0)
	set(index++, int.byte1)
	set(index++, int.byte2)
	set(index, int.byte3)
}

inline fun Memory.byte(index: Int): Byte = byteArray[index]

fun Memory.int(index: Index): Int {
	var index = index
	val byte0 = byte(index++)
	val byte1 = byte(index++)
	val byte2 = byte(index++)
	val byte3 = byte(index)
	return leo.base.int(byte3, byte2, byte1, byte0)
}

val Memory.asField get() = _memory.invoke(byteArray.seq.map { asField }.stack.value)

fun memory(vararg ints: Int) = ints.size.shl(2).sizeMemory.pointer(*ints).memory