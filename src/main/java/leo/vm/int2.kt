package leo.vm

import leo.base.Variable
import leo.binary.Bit

data class Int2(
	val hiInt1: Int1,
	val loInt1: Int1)

val Int2.notOp: Op
	get() =
		sequenceOp(
			hiInt1.notOp,
			loInt1.notOp)

fun Int2.andOp(int2: Int2): Op =
	sequenceOp(
		hiInt1.andOp(int2.hiInt1),
		loInt1.andOp(int2.loInt1))

fun Int2.orOp(int2: Int2): Op =
	sequenceOp(
		hiInt1.orOp(int2.hiInt1),
		loInt1.orOp(int2.loInt1))

fun Int2.xorOp(int2: Int2): Op =
	sequenceOp(
		hiInt1.xorOp(int2.hiInt1),
		loInt1.xorOp(int2.loInt1))

fun Int2.incOp(carry: Variable<Bit>): Op =
	sequenceOp(
		loInt1.incOp(carry),
		hiInt1.incOp(carry))

fun Int2.addOp(int2: Int2, carry: Variable<Bit>): Op =
	sequenceOp(
		loInt1.addOp(int2.loInt1, carry),
		hiInt1.addOp(int2.hiInt1, carry))

