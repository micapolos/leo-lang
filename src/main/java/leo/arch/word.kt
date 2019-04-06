package leo.arch

import leo.base.Seq
import leo.binary.Bit
import leo32.base.*

sealed class Word<A: Arch>
data class Word32(val i32: I32): Word<Arch32>()
data class Word64(val i64: I64): Word<Arch64>()

val I32.word: Word32 get() = Word32(this)
val I64.word: Word64 get() = Word64(this)

fun <A: Arch> I32.word(): Word<A> = Word32(this).cast()
fun <A: Arch> I64.word(): Word<A> = Word64(this).cast()

val Word<*>.word32 get() = this as Word32
val Word<*>.word64 get() = this as Word64

@Suppress("UNCHECKED_CAST")
fun <A: Arch> Word<*>.cast() =
	this as Word<A>

@Suppress("UNCHECKED_CAST")
val <A: Arch> Word<A>.arch: A get() =
	when (this) {
		is Word32 -> arch32 as A
		is Word64 -> arch64 as A
	}

val <A: Arch> Word<A>.incWrap: Word<A> get() =
	when (this) {
		is Word32 -> i32.incWrap.word()
		is Word64 -> i64.incWrap.word()
	}

val <A: Arch> Word<A>.decWrap: Word<A> get() =
	when (this) {
		is Word32 -> i32.decWrap.word()
		is Word64 -> i64.decWrap.word()
	}

val <A: Arch> Word<A>.inc: Word<A> get() =
	when (this) {
		is Word32 -> i32.inc.word()
		is Word64 -> i64.inc.word()
	}

val <A: Arch> Word<A>.dec: Word<A> get() =
	when (this) {
		is Word32 -> i32.dec.word()
		is Word64 -> i64.dec.word()
	}

operator fun <A: Arch> Word<A>.plus(word: Word<A>): Word<A> =
	when (this) {
		is Word32 -> i32.plus(word.word32.i32).word()
		is Word64 -> i64.plus(word.word64.i64).word()
	}

operator fun <A: Arch> Word<A>.minus(word: Word<A>): Word<A> =
	when (this) {
		is Word32 -> i32.minus(word.word32.i32).word()
		is Word64 -> i64.minus(word.word64.i64).word()
	}

infix fun <A: Arch> Word<A>.and(word: Word<A>): Word<A> =
	when (this) {
		is Word32 -> i32.and(word.word32.i32).word()
		is Word64 -> i64.and(word.word64.i64).word()
	}

infix fun <A: Arch> Word<A>.or(word: Word<A>): Word<A> =
	when (this) {
		is Word32 -> i32.or(word.word32.i32).word()
		is Word64 -> i64.or(word.word64.i64).word()
	}

infix fun <A: Arch> Word<A>.maskPair(word: Word<A>): Pair<Word<A>, Word<A>> =
	when (this) {
		is Word32 -> i32.maskPair(word.word32.i32).run { first.word<A>() to second.word<A>() }
		is Word64 -> i64.maskPair(word.word64.i64).run { first.word<A>() to second.word<A>() }
	}

infix fun <A: Arch> Word<A>.shr(word: Word<A>): Word<A> =
	when (this) {
		is Word32 -> i32.shr(word.word32.i32).word()
		is Word64 -> i64.shr(word.word64.i64).word()
	}

infix fun <A: Arch> Word<A>.shl(word: Word<A>): Word<A> =
	when (this) {
		is Word32 -> i32.shl(word.word32.i32).word()
		is Word64 -> i64.shl(word.word64.i64).word()
	}

val <A: Arch> Word<A>.shr1: Word<A> get() =
	when (this) {
		is Word32 -> i32.shr1.word()
		is Word64 -> i64.shr1.word()
	}

val <A: Arch> Word<A>.shl1: Word<A> get() =
	when (this) {
		is Word32 -> i32.shl1.word()
		is Word64 -> i64.shl1.word()
	}

val <A: Arch> Word<A>.bit get() =
	when (this) {
		is Word32 -> i32.bit
		is Word64 -> i64.bit
	}

operator fun <A: Arch> Word<A>.compareTo(word: Word<A>): Int =
	when (this) {
		is Word32 -> i32.compareTo(word.word32.i32)
		is Word64 -> i64.compareTo(word.word64.i64)
	}

val <A: Arch> Word<A>.bitSeq: Seq<Bit> get() =
	when (this) {
		is Word32 -> i32.bitSeq
		is Word64 -> i64.bitSeq
	}
