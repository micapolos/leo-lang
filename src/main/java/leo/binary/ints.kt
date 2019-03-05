package leo.binary

import leo.base.*

object Int0
data class Int1(val hsb: Bit, val lo: Int0)
data class Int2(val hsb: Bit, val lo: Int1)
data class Int3(val hsb: Bit, val lo: Int2)
data class Int4(val hsb: Bit, val lo: Int3)
data class Int5(val hsb: Bit, val lo: Int4)

val int0 = Int0
fun Bit.lo(lo: Int0) = Int1(this, lo)
fun Bit.lo(lo: Int1) = Int2(this, lo)
fun Bit.lo(lo: Int2) = Int3(this, lo)
fun Bit.lo(lo: Int3) = Int4(this, lo)
fun Bit.lo(lo: Int4) = Int5(this, lo)

fun Int0.hsb(hsb: Bit) = Int1(hsb, this)
fun Int1.hsb(hsb: Bit) = Int2(hsb, this)
fun Int2.hsb(hsb: Bit) = Int3(hsb, this)
fun Int3.hsb(hsb: Bit) = Int4(hsb, this)
fun Int4.hsb(hsb: Bit) = Int5(hsb, this)

val zeroInt0 = int0
val zeroInt1 = 0.bit.lo(zeroInt0)
val zeroInt2 = 0.bit.lo(zeroInt1)
val zeroInt3 = 0.bit.lo(zeroInt2)
val zeroInt4 = 0.bit.lo(zeroInt3)
val zeroInt5 = 0.bit.lo(zeroInt4)

val Int0.int get() = 0
val Int1.int get() = hsb.int.shl(0).or(lo.int)
val Int2.int get() = hsb.int.shl(1).or(lo.int)
val Int3.int get() = hsb.int.shl(2).or(lo.int)
val Int4.int get() = hsb.int.shl(3).or(lo.int)
val Int5.int get() = hsb.int.shl(4).or(lo.int)

val Int.wrappingInt0 get() = int0
val Int.wrappingInt1 get() = and(1.shl(0)).bit.lo(wrappingInt0)
val Int.wrappingInt2 get() = and(1.shl(1)).bit.lo(wrappingInt1)
val Int.wrappingInt3 get() = and(1.shl(2)).bit.lo(wrappingInt2)
val Int.wrappingInt4 get() = and(1.shl(3)).bit.lo(wrappingInt3)
val Int.wrappingInt5 get() = and(1.shl(4)).bit.lo(wrappingInt4)

fun <L : Any, I : Any> incOrNull(hsb: Bit, loIncOrNull: L?, loZero: L, hsbLoFn: Bit.(L) -> I): I? =
	loIncOrNull.ifNotNullOr(
		{ hsb.hsbLoFn(it).orNull },
		{ if (hsb.isZero) 1.bit.hsbLoFn(loZero) else null })

val Int0.incOrNull get() = null as Int0?
val Int1.incOrNull get() = incOrNull(hsb, lo.incOrNull, zeroInt0) { lo(it) }
val Int2.incOrNull get() = incOrNull(hsb, lo.incOrNull, zeroInt1) { lo(it) }
val Int3.incOrNull get() = incOrNull(hsb, lo.incOrNull, zeroInt2) { lo(it) }
val Int4.incOrNull get() = incOrNull(hsb, lo.incOrNull, zeroInt3) { lo(it) }
val Int5.incOrNull get() = incOrNull(hsb, lo.incOrNull, zeroInt4) { lo(it) }

fun <I, R> R.forEach(first: I, incFn: I.() -> I?, fn: R.(I) -> R): R {
	var r = this
	var i = first.orNull
	while (i != null) {
		r = r.fn(i)
		i = incFn(i)
	}
	return r
}

fun <R> R.forEachInt0(fn: R.(Int0) -> R): R = forEach(zeroInt0, { incOrNull }, fn)
fun <R> R.forEachInt1(fn: R.(Int1) -> R): R = forEach(zeroInt1, { incOrNull }, fn)
fun <R> R.forEachInt2(fn: R.(Int2) -> R): R = forEach(zeroInt2, { incOrNull }, fn)
fun <R> R.forEachInt3(fn: R.(Int3) -> R): R = forEach(zeroInt3, { incOrNull }, fn)
fun <R> R.forEachInt4(fn: R.(Int4) -> R): R = forEach(zeroInt4, { incOrNull }, fn)
fun <R> R.forEachInt5(fn: R.(Int5) -> R): R = forEach(zeroInt5, { incOrNull }, fn)

val Int0.bits get() = emptySequence<Bit>()
val Int1.bits get() = Sequence { hsb.thenNonEmptySequence(lo.bits) }
val Int2.bits get() = Sequence { hsb.thenNonEmptySequence(lo.bits) }
val Int3.bits get() = Sequence { hsb.thenNonEmptySequence(lo.bits) }
val Int4.bits get() = Sequence { hsb.thenNonEmptySequence(lo.bits) }
val Int5.bits get() = Sequence { hsb.thenNonEmptySequence(lo.bits) }
