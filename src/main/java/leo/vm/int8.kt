package leo.vm

import leo.base.Bit
import leo.base.Variable

data class Int8(
	val hiInt4: Int4,
	val loInt4: Int4)

val Int8.notOp: Op
	get() =
		sequenceOp(
			hiInt4.notOp,
			loInt4.notOp)

fun Int8.andOp(int8: Int8): Op =
	sequenceOp(
		hiInt4.andOp(int8.hiInt4),
		loInt4.andOp(int8.loInt4))

fun Int8.orOp(int8: Int8): Op =
	sequenceOp(
		hiInt4.orOp(int8.hiInt4),
		loInt4.orOp(int8.loInt4))

fun Int8.xorOp(int8: Int8): Op =
	sequenceOp(
		hiInt4.xorOp(int8.hiInt4),
		loInt4.xorOp(int8.loInt4))

fun Int8.incOp(carry: Variable<Bit>): Op =
	sequenceOp(
		loInt4.incOp(carry),
		hiInt4.incOp(carry))

fun Int8.addOp(int8: Int8, carry: Variable<Bit>): Op =
	sequenceOp(
		loInt4.addOp(int8.loInt4, carry),
		hiInt4.addOp(int8.hiInt4, carry))

