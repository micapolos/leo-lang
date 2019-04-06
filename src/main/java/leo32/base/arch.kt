package leo32.base

import leo.base.seq

sealed class Arch
object Arch32: Arch()
object Arch64: Arch()

val arch32 = Arch32
val arch64 = Arch64

val Arch.zero: Word<Arch>
	get() =
	when (this) {
		is Arch32 -> 0.i32.word()
		is Arch64 -> 0.i64.word()
	}

val Arch.max: Word<Arch>
	get() =
		when (this) {
			is Arch32 -> (-1).i32.word()
			is Arch64 -> (-1L).i64.word()
		}

val archSeq get() =
	seq(arch32, arch64)