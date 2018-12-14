package leo.vm

import leo.base.Bit
import leo.base.Variable

data class Int16(
	val hiInt8: Int8,
	val loInt8: Int8)

val Int16.notOp: Op
	get() =
		sequenceOp(
			hiInt8.notOp,
			loInt8.notOp)

fun Int16.andOp(int16: Int16): Op =
	sequenceOp(
		hiInt8.andOp(int16.hiInt8),
		loInt8.andOp(int16.loInt8))

fun Int16.orOp(int16: Int16): Op =
	sequenceOp(
		hiInt8.orOp(int16.hiInt8),
		loInt8.orOp(int16.loInt8))

fun Int16.xorOp(int16: Int16): Op =
	sequenceOp(
		hiInt8.xorOp(int16.hiInt8),
		loInt8.xorOp(int16.loInt8))

fun Int16.incOp(carry: Variable<Bit>): Op =
	sequenceOp(
		loInt8.incOp(carry),
		hiInt8.incOp(carry))

fun Int16.addOp(int16: Int16, carry: Variable<Bit>): Op =
	sequenceOp(
		loInt8.addOp(int16.loInt8, carry),
		hiInt8.addOp(int16.hiInt8, carry))

