package leo.vm

import leo.base.Bit
import leo.base.Variable

data class Int4(
	val hiInt2: Int2,
	val loInt2: Int2)

val Int4.notOp: Op
	get() =
		sequenceOp(
			hiInt2.notOp,
			loInt2.notOp)

fun Int4.andOp(int4: Int4): Op =
	sequenceOp(
		hiInt2.andOp(int4.hiInt2),
		loInt2.andOp(int4.loInt2))

fun Int4.orOp(int4: Int4): Op =
	sequenceOp(
		hiInt2.orOp(int4.hiInt2),
		loInt2.orOp(int4.loInt2))

fun Int4.xorOp(int4: Int4): Op =
	sequenceOp(
		hiInt2.xorOp(int4.hiInt2),
		loInt2.xorOp(int4.loInt2))

fun Int4.incOp(carry: Variable<Bit>): Op =
	sequenceOp(
		loInt2.incOp(carry),
		hiInt2.incOp(carry))

fun Int4.addOp(int4: Int4, carry: Variable<Bit>): Op =
	sequenceOp(
		loInt2.addOp(int4.loInt2, carry),
		hiInt2.addOp(int4.hiInt2, carry))

