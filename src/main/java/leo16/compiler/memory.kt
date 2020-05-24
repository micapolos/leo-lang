package leo16.compiler

import leo.base.map
import leo.base.stack
import leo.binary.seq
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
operator fun Memory.set(index: Int, byte: Byte) {
	byteArray[index] = byte
}

operator fun Memory.get(index: Int) = byteArray[index]

val Memory.asField get() = _memory.invoke(byteArray.seq.map { asField }.stack.value)

fun memory(vararg ints: Int) = ints.size.shl(2).sizeMemory.pointer(*ints).memory