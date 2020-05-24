package leo16.compiler

import leo.base.Effect
import leo.base.bind
import leo.base.byte0
import leo.base.byte1
import leo.base.byte2
import leo.base.byte3
import leo.base.effect
import leo.base.int
import leo.base.iterate

data class Pointer(val memory: Memory, val index: Int)

fun Memory.pointer(index: Int) = Pointer(this, index)
val Pointer.inc get() = memory.pointer(index.inc())

fun Pointer.write(byte: Byte): Pointer =
	inc.also { memory[index] = byte }

fun Pointer.write(int: Int): Pointer =
	write(int.byte0).write(int.byte1).write(int.byte2).write(int.byte3)

fun Pointer.writeZeros(count: Int): Pointer =
	iterate(count) { write(0.toByte()) }

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

fun Pointer.readZeros(count: Int): Pointer =
	iterate(count) { readByte.state }
