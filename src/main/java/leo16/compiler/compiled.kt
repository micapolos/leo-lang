package leo16.compiler

import leo.base.byte0
import leo.base.byte1
import leo.base.byte2
import leo.base.byte3
import leo.base.clampedByte
import leo.base.fold
import leo.base.iterate
import leo16.Field
import leo16.invoke
import leo16.names.*

data class Compiled(val binary: Binary, val size: Int) {
	override fun toString() = asField.toString()
}

val emptyCompiled get() = Compiled(emptyBinary, 0)

operator fun Compiled.plus(byte: Byte) =
	Compiled(binary.plus(byte), size.inc())

operator fun Compiled.plus(int: Int): Compiled =
	plus(Alignment.ALIGNMENT_4).plus(int.byte0).plus(int.byte1).plus(int.byte2).plus(int.byte3)

tailrec operator fun Compiled.plus(alignment: Alignment): Compiled =
	if (size and alignment.mask == 0) this
	else plus(0.clampedByte).plus(alignment)

val Compiled.asField: Field
	get() =
		_compiled(
			binary.asField,
			_size(size.asField))

fun compiled(vararg ints: Int) =
	emptyCompiled.fold(ints.toList()) { plus(it) }

fun Compiled.plusZeros(size: Int): Compiled =
	iterate(size) { plus(0.toByte()) }