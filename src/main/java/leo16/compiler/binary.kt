package leo16.compiler

import leo.base.EnumBit
import leo.base.enumBit
import leo.base.fold
import leo.base.int
import leo.base.littleEndianBitSeq
import leo13.Stack
import leo13.array
import leo13.map
import leo13.push
import leo13.stack
import leo16.Field
import leo16.invoke
import leo16.names.*

data class Binary(val bitStack: Stack<EnumBit>, val size: Int) {
	override fun toString() = asField.toString()
}

val emptyBinary = Binary(stack(), 0)
fun binary(vararg bits: Int) = Binary(stack(*bits.map { it.enumBit }.toTypedArray()), bits.size)

val Binary.asField: Field
	get() =
		_binary(bitStack.map { int.toString() }.array.joinToString()())

fun Binary.plus(bit: EnumBit) = Binary(bitStack.push(bit), size.inc())

tailrec fun Binary.align(alignment: Alignment): Binary =
	if (size.and(alignment.indexMask) == 0) this
	else plus(EnumBit.ZERO).align(alignment)

fun Binary.plusWithBitCount(int: Int, bitCount: Int): Binary =
	fold(int.littleEndianBitSeq(bitCount)) { plus(it) }

fun Binary.plus(int: Int): Binary =
	fold(int.littleEndianBitSeq(32)) { plus(it) }
