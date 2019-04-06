package leo.arch

import leo.base.fail
import leo.base.seq
import leo32.base.i32
import leo32.base.i64

sealed class Arch
object Arch32: Arch()
object Arch64: Arch()

val arch32 = Arch32
val arch64 = Arch64

val <A: Arch> A.zero: Word<A> get() =
	when (this) {
		is Arch32 -> 0.i32.word()
		is Arch64 -> 0.i64.word()
		else -> fail
	}

val <A: Arch> A.one: Word<A> get() =
	zero.inc

val <A: Arch> A.max: Word<A> get() =
	when (this) {
		is Arch32 -> (-1).i32.word()
		is Arch64 -> (-1L).i64.word()
		else -> fail
	}

val <A: Arch> A.bits: Word<A> get() =
	when (this) {
		is Arch32 -> 32.i32.word()
		is Arch64 -> 64.i64.word()
		else -> fail
	}

val <A: Arch> A.hsb: Word<A> get() =
	one.shl(bits.dec)

val archSeq get() =
	seq(arch32, arch64)