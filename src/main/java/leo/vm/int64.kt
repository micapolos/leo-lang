package leo.vm

import leo.base.Bit
import leo.base.Variable

data class Int64(
	val hiInt32: Int32,
	val loInt32: Int32)

val Int64.notOp: Op
	get() =
		sequenceOp(
			hiInt32.notOp,
			loInt32.notOp)

fun Int64.andOp(int64: Int64): Op =
	sequenceOp(
		hiInt32.andOp(int64.hiInt32),
		loInt32.andOp(int64.loInt32))

fun Int64.orOp(int64: Int64): Op =
	sequenceOp(
		hiInt32.orOp(int64.hiInt32),
		loInt32.orOp(int64.loInt32))

fun Int64.xorOp(int64: Int64): Op =
	sequenceOp(
		hiInt32.xorOp(int64.hiInt32),
		loInt32.xorOp(int64.loInt32))

fun Int64.incOp(carry: Variable<Bit>): Op =
	sequenceOp(
		loInt32.incOp(carry),
		hiInt32.incOp(carry))

fun Int64.addOp(int64: Int64, carry: Variable<Bit>): Op =
	sequenceOp(
		loInt32.addOp(int64.loInt32, carry),
		hiInt32.addOp(int64.hiInt32, carry))

