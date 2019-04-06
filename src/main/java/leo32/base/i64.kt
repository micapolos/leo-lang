@file:Suppress("unused")

package leo32.base

import leo.base.*
import leo.binary.*

data class I64(
	val long: Long)

val Int.i64
	get() =
		toLong().and(0xFFFFFFFF).i64

val Long.i64
	get() =
		I64(this)

val Zero.i64 get() = 0L.i64
val One.i64 get() = 1L.i64

val Min.i64 get() = 0L.i64
val Max.i64 get() = (-1L).i64

val hsbI64 get() = 1L.shl(63).i64

val I64.isZero
	get() =
		long == 0L

val I64.incWrap
	get() =
		long.inc().i64

val I64.decWrap
	get() =
		long.dec().i64

val I64.inc
	get() =
		if (this == max.i64) error("i64 overflow")
		else long.inc().i64

val I64.dec
	get() =
		if (this == min.i64) error("i64 underflow")
		else long.dec().i64

// TODO: Throw on overflow
operator fun I64.plus(i64: I64) =
	long.plus(i64.long).i64

// TODO: Throw on overflow
operator fun I64.minus(i64: I64) =
	long.minus(i64.long).i64

infix fun I64.and(i64: I64) =
	long.and(i64.long).i64

infix fun I64.or(i64: I64) =
	long.or(i64.long).i64

infix fun I64.maskPair(i64: I64): Pair<I64, I64> =
	and(i64.inv) to and(i64)

val I64.inv
	get() =
		long.inv().i64

fun I64.shr(i64: I64) =
	long.ushr(i64.long.toInt()).i64

fun I64.shl(i64: I64) =
	long.shl(i64.long.toInt()).i64

val I64.shr1
	get() =
		shr(one.i64)

val I64.shl1
	get() =
		shl(zero.i64)

val I64.bit
	get() =
		if (long == 0L) zero.bit else one.bit

operator fun I64.compareTo(i64: I64) =
	if (long >= 0)
		if (i64.long >= 0) long.compareTo(i64.long)
		else -1
	else
		if (i64.long >= 0) 1
		else long.compareTo(i64.long).unaryMinus()

fun I64.bitSeq(bitMask: I64): Seq<Bit> =
	Seq {
		if (bitMask.isZero) null
		else and(bitMask).bit then bitSeq(bitMask.shr1)
	}

val I64.bitSeq: Seq<Bit> get() =
	bitSeq(hsbI64)

// === primitives

val Bit.i64
	get() =
		int.toLong().i64

val Byte.i64
	get() =
		uint.toLong().i64

val Char.i64
	get() =
		int.toLong().i64
