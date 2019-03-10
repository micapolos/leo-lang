package leo.vm

import leo.base.EnumBit
import leo.base.Variable

data class Int32(
	val hiInt16: Int16,
	val loInt16: Int16)

val Int32.notOp: Op
	get() =
		sequenceOp(
			hiInt16.notOp,
			loInt16.notOp)

fun Int32.andOp(int32: Int32): Op =
	sequenceOp(
		hiInt16.andOp(int32.hiInt16),
		loInt16.andOp(int32.loInt16))

fun Int32.orOp(int32: Int32): Op =
	sequenceOp(
		hiInt16.orOp(int32.hiInt16),
		loInt16.orOp(int32.loInt16))

fun Int32.xorOp(int32: Int32): Op =
	sequenceOp(
		hiInt16.xorOp(int32.hiInt16),
		loInt16.xorOp(int32.loInt16))

fun Int32.incOp(carry: Variable<EnumBit>): Op =
	sequenceOp(
		loInt16.incOp(carry),
		hiInt16.incOp(carry))

fun Int32.addOp(int32: Int32, carry: Variable<EnumBit>): Op =
	sequenceOp(
		loInt16.addOp(int32.loInt16, carry),
		hiInt16.addOp(int32.hiInt16, carry))

