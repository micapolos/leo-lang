package leo.binary

import leo.base.Stream
import leo.base.ifNotNull
import leo.base.onlySeq
import leo.base.read

sealed class Bit
data class ZeroBit(val zero: Zero) : Bit()
data class OneBit(val one: One) : Bit()

val Zero.bit: Bit get() = ZeroBit(this)
val One.bit: Bit get() = OneBit(this)

inline fun <R> Bit.match(zeroFn: Zero.() -> R, oneFn: One.() -> R) =
	when (this) {
		is ZeroBit -> zero.zeroFn()
		is OneBit -> one.oneFn()
	}

val Bit.boolean
	get() =
		match({ boolean }, { boolean })

val Boolean.bit
	get() =
		if (this) one.bit else zero.bit

val Bit.int
	get() =
		match({ int }, { int })

val Bit.char
	get() =
		match({ char }, { char })

val Bit.inverse
	get() =
		match({ one.bit }, { zero.bit })

infix fun Bit.nand(bit: Bit) =
	and(bit).inverse

fun Bit.and(bit: Bit) =
	boolean.and(bit.boolean).bit

fun Bit.or(bit: Bit) =
	boolean.or(bit.boolean).bit

fun Bit.xor(bit: Bit) =
	boolean.xor(bit.boolean).bit

val Int.bitOrNull
	get() =
		when (this) {
			0 -> zero.bit
			1 -> one.bit
			else -> null
		}

val Int.bit
	get() =
		if (this == 0) zero.bit
		else one.bit

val Int.lastBit: Bit
	get() =
		and(1).bit

val Int.clampedBit
	get() = bit

fun <V> Bit.ifZero(value: V, fn: (V) -> V): V =
	match({ fn(value) }, { value })

fun Appendable.append(bit: Bit): Appendable =
	append(bit.char)

fun Appendable.appendBit(bitStream: Stream<Bit>): Appendable =
	read(bitStream) { bit, nextOrNull ->
		append(bit).ifNotNull(nextOrNull, Appendable::appendBit)
	}

val Bit.bitSeq get() = onlySeq

val Bit.isZero
	get() =
		match({ true }, { false })
