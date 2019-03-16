package leo.binary

import leo.base.onlySeq

sealed class Bit

data class ZeroBit(
	val zero: Zero
) : Bit()

data class OneBit(
	val one: One
) : Bit()

val Zero.bit: Bit
	get() =
		ZeroBit(this)

val One.bit: Bit
	get() =
		OneBit(this)

inline fun <R> Bit.match(zeroFn: Zero.() -> R, oneFn: One.() -> R) =
	when (this) {
		is ZeroBit -> zero.zeroFn()
		is OneBit -> one.oneFn()
	}

val Bit.isOne
	get() =
		match({ isOne }, { isOne })

val Boolean.bit
	get() =
		if (this) one.bit else zero.bit

val Bit.int
	get() =
		match({ int }, { int })

val Bit.digitChar
	get() =
		match({ digitChar }, { digitChar })

val Bit.inverse
	get() =
		match({ one.bit }, { zero.bit })

infix fun Bit.nand(bit: Bit) =
	and(bit).inverse

fun Bit.and(bit: Bit) =
	isOne.and(bit.isOne).bit

fun Bit.or(bit: Bit) =
	isOne.or(bit.isOne).bit

val Int.bit
	get() =
		if (this == 0) zero.bit
		else one.bit

val Int.clampedBit
	get() = bit

val Long.clampedBit
	get() = (this != 0L).bit

fun Appendable.append(bit: Bit): Appendable =
	append(bit.digitChar)

val Bit.bitSeq get() = onlySeq

val Bit.isZero
	get() =
		match({ true }, { false })
