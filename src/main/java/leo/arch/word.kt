package leo.arch

import leo32.base.I32
import leo32.base.I64
import leo32.base.dec
import leo32.base.inc

sealed class Word<A: Arch>
data class Word32(val i32: I32): Word<Arch32>()
data class Word64(val i64: I64): Word<Arch64>()

val I32.word: Word32 get() = Word32(this)
val I64.word: Word64 get() = Word64(this)

fun <A: Arch> I32.word(): Word<A> = Word32(this).cast()
fun <A: Arch> I64.word(): Word<A> = Word64(this).cast()

@Suppress("UNCHECKED_CAST")
fun <A: Arch> Word<*>.cast() =
	this as Word<A>

val <A: Arch> Word<A>.inc: Word<A>
	get() =
	when (this) {
		is Word32 -> i32.inc.word()
		is Word64 -> i64.inc.word()
	}

val <A: Arch> Word<A>.dec: Word<A>
	get() =
	when (this) {
		is Word32 -> i32.dec.word()
		is Word64 -> i64.dec.word()
	}

