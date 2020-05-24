package leo16.compiler

import leo.base.Effect
import leo.base.effect
import leo.base.fold
import leo.base.iterate
import leo16.invoke
import leo16.names.*

data class Pointer(val memory: Memory, val index: Int) {
	override fun toString() = asField.toString()
}

fun Memory.pointer(index: Int) = Pointer(this, index)
val Memory.startPointer get() = pointer(0)
fun Memory.pointer(vararg ints: Int) = startPointer.fold(ints.toList()) { write(it) }

val Pointer.asField
	get() =
		_pointer(memory.asField, _index(index.asField))

val Pointer.inc get() = memory.pointer(index.inc())

fun Pointer.write(byte: Byte): Pointer =
	inc.also { memory.set(index, byte) }

fun Pointer.write(int: Int): Pointer =
	memory.pointer(index + 4).also { memory.set(index, int) }

val Pointer.writeZero: Pointer
	get() =
		write(0.toByte())

fun Pointer.writeZeros(count: Int): Pointer =
	iterate(count) { writeZero }

val Pointer.readByte: Effect<Pointer, Byte>
	get() =
		inc effect memory.byte(index)

val Pointer.readInt: Effect<Pointer, Int>
	get() =
		memory.pointer(index + 4) effect memory.int(index)

fun Pointer.skip(count: Int): Pointer =
	iterate(count) { readByte.state }

tailrec fun Pointer.write(alignment: Alignment): Pointer =
	if (index.and(alignment.mask) == 0) this
	else writeZero.write(alignment)

fun Pointer.set(alignment: Alignment): Pointer =
	memory.pointer(alignment.align(index))
