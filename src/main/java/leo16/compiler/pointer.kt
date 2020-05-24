package leo16.compiler

import leo.base.Effect
import leo.base.bind
import leo.base.byte0
import leo.base.byte1
import leo.base.byte2
import leo.base.byte3
import leo.base.effect
import leo.base.fold
import leo.base.int
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
	inc.also { memory[index] = byte }

fun Pointer.write(int: Int): Pointer =
	write(int.byte0).write(int.byte1).write(int.byte2).write(int.byte3)

val Pointer.writeZero: Pointer
	get() =
		write(0.toByte())

fun Pointer.writeZeros(count: Int): Pointer =
	iterate(count) { writeZero }

val Pointer.readByte: Effect<Pointer, Byte>
	get() =
		inc effect memory[index]

val Pointer.readInt: Effect<Pointer, Int>
	get() =
		readByte.bind { byte0 ->
			readByte.bind { byte1 ->
				readByte.bind { byte2 ->
					readByte.bind { byte3 ->
						this effect int(byte3, byte2, byte1, byte0)
					}
				}
			}
		}

fun Pointer.skip(count: Int): Pointer =
	iterate(count) { readByte.state }

tailrec fun Pointer.write(alignment: Alignment): Pointer =
	if (index.and(alignment.mask) == 0) this
	else writeZero.write(alignment)

fun Pointer.set(alignment: Alignment): Pointer =
	memory.pointer(alignment.align(index))
