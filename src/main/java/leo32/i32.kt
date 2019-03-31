@file:Suppress("unused")

package leo32

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