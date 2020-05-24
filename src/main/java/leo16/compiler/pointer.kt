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

class Pointer(val byteArray: ByteArray, val index: Int)

fun ByteArray.pointer(index: Int) = Pointer(this, index)
val Pointer.inc get() = byteArray.pointer(index.inc())

fun Pointer.write(byte: Byte): Pointer =
	inc.also { byteArray[index] = byte }

fun Pointer.write(int: Int): Pointer =
	write(int.byte0).write(int.byte1).write(int.byte2).write(int.byte3)

fun Pointer.writeZeros(count: Int): Pointer =
	iterate(count) { write(0.toByte()) }

val Pointer.readByte: Effect<Pointer, Byte>
	get() =
		inc effect byteArray[index]

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
