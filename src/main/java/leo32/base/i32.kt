@file:Suppress("unused")

package leo32.base

import leo.base.*
import leo.binary.*

data class I32(
	val int: Int)

val Int.i32
	get() =
		I32(this)

val Zero.i32 get() = 0.i32
val One.i32 get() = 1.i32

val Min.i32 get() = 0.i32
val Max.i32 get() = (-1).i32

val hsbI32 get() = 1.shl(31).i32

val I32.isZero
	get() =
		int == 0

val I32.inc
	get() =
		if (this == max.i32) error("i32 overflow")
		else int.inc().i32

val I32.dec
	get() =
		if (this == min.i32) error("i32 underflow")
		else int.dec().i32

// TODO: Throw on overflow
operator fun I32.plus(i32: I32) =
	int.plus(i32.int).i32

// TODO: Throw on overflow
operator fun I32.minus(i32: I32) =
	int.minus(i32.int).i32

infix fun I32.and(i32: I32) =
	int.and(i32.int).i32

infix fun I32.or(i32: I32) =
	int.or(i32.int).i32

infix fun I32.maskPair(i32: I32): Pair<I32, I32> =
	and(i32.inv) to and(i32)

val I32.inv
	get() =
		int.inv().i32

fun I32.shr(i32: I32) =
	int.ushr(i32.int).i32

fun I32.shl(i32: I32) =
	int.shl(i32.int).i32

val I32.shr1
	get() =
		shr(one.i32)

val I32.shl1
	get() =
		shl(zero.i32)

val I32.bit
	get() =
		int.bit

operator fun I32.compareTo(i32: I32) =
	if (int >= 0)
		if (i32.int >= 0) int.compareTo(i32.int)
		else -1
	else
		if (i32.int >= 0) 1
		else int.compareTo(i32.int).unaryMinus()

fun I32.bitSeq(bitMask: I32): Seq<Bit> =
	Seq {
		if (bitMask.isZero) null
		else and(bitMask).bit.thenSeqNode(bitSeq(bitMask.shr1))
	}

val I32.bitSeq: Seq<Bit> get() =
	bitSeq(hsbI32)

// === primitives

val Bit.i32
	get() =
		int.i32

val Byte.i32
	get() =
		uint.i32

val Char.i32
	get() =
		int.i32
