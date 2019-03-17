@file:Suppress("unused")

package leo.binary

import leo.base.*

object Int0
data class Int1(val hsb: Bit, val lo: Int0)
data class Int2(val hsb: Bit, val lo: Int1)
data class Int3(val hsb: Bit, val lo: Int2)
data class Int4(val hsb: Bit, val lo: Int3)
data class Int5(val hsb: Bit, val lo: Int4)
data class Int6(val hsb: Bit, val lo: Int5)
data class Int7(val hsb: Bit, val lo: Int6)
data class Int8(val hsb: Bit, val lo: Int7)
data class Int9(val hsb: Bit, val lo: Int8)
data class Int10(val hsb: Bit, val lo: Int9)
data class Int11(val hsb: Bit, val lo: Int10)
data class Int12(val hsb: Bit, val lo: Int11)
data class Int13(val hsb: Bit, val lo: Int12)
data class Int14(val hsb: Bit, val lo: Int13)
data class Int15(val hsb: Bit, val lo: Int14)
data class Int16(val hsb: Bit, val lo: Int15)

val int0 = Int0
fun Bit.lo(lo: Int0) = Int1(this, lo)
fun Bit.lo(lo: Int1) = Int2(this, lo)
fun Bit.lo(lo: Int2) = Int3(this, lo)
fun Bit.lo(lo: Int3) = Int4(this, lo)
fun Bit.lo(lo: Int4) = Int5(this, lo)
fun Bit.lo(lo: Int5) = Int6(this, lo)
fun Bit.lo(lo: Int6) = Int7(this, lo)
fun Bit.lo(lo: Int7) = Int8(this, lo)
fun Bit.lo(lo: Int8) = Int9(this, lo)
fun Bit.lo(lo: Int9) = Int10(this, lo)
fun Bit.lo(lo: Int10) = Int11(this, lo)
fun Bit.lo(lo: Int11) = Int12(this, lo)
fun Bit.lo(lo: Int12) = Int13(this, lo)
fun Bit.lo(lo: Int13) = Int14(this, lo)
fun Bit.lo(lo: Int14) = Int15(this, lo)
fun Bit.lo(lo: Int15) = Int16(this, lo)

fun Int0.hsb(hsb: Bit) = Int1(hsb, this)
fun Int1.hsb(hsb: Bit) = Int2(hsb, this)
fun Int2.hsb(hsb: Bit) = Int3(hsb, this)
fun Int3.hsb(hsb: Bit) = Int4(hsb, this)
fun Int4.hsb(hsb: Bit) = Int5(hsb, this)
fun Int5.hsb(hsb: Bit) = Int6(hsb, this)
fun Int6.hsb(hsb: Bit) = Int7(hsb, this)
fun Int7.hsb(hsb: Bit) = Int8(hsb, this)
fun Int8.hsb(hsb: Bit) = Int9(hsb, this)
fun Int9.hsb(hsb: Bit) = Int10(hsb, this)
fun Int10.hsb(hsb: Bit) = Int11(hsb, this)
fun Int11.hsb(hsb: Bit) = Int12(hsb, this)
fun Int12.hsb(hsb: Bit) = Int13(hsb, this)
fun Int13.hsb(hsb: Bit) = Int14(hsb, this)
fun Int14.hsb(hsb: Bit) = Int15(hsb, this)
fun Int15.hsb(hsb: Bit) = Int16(hsb, this)

val zeroInt0 = int0
val zeroInt1 = 0.bit.lo(zeroInt0)
val zeroInt2 = 0.bit.lo(zeroInt1)
val zeroInt3 = 0.bit.lo(zeroInt2)
val zeroInt4 = 0.bit.lo(zeroInt3)
val zeroInt5 = 0.bit.lo(zeroInt4)
val zeroInt6 = 0.bit.lo(zeroInt5)
val zeroInt7 = 0.bit.lo(zeroInt6)
val zeroInt8 = 0.bit.lo(zeroInt7)
val zeroInt9 = 0.bit.lo(zeroInt8)
val zeroInt10 = 0.bit.lo(zeroInt9)
val zeroInt11 = 0.bit.lo(zeroInt10)
val zeroInt12 = 0.bit.lo(zeroInt11)
val zeroInt13 = 0.bit.lo(zeroInt12)
val zeroInt14 = 0.bit.lo(zeroInt13)
val zeroInt15 = 0.bit.lo(zeroInt14)
val zeroInt16 = 0.bit.lo(zeroInt15)

val Int0.int get() = 0
val Int1.int get() = hsb.int.shl(0).or(lo.int)
val Int2.int get() = hsb.int.shl(1).or(lo.int)
val Int3.int get() = hsb.int.shl(2).or(lo.int)
val Int4.int get() = hsb.int.shl(3).or(lo.int)
val Int5.int get() = hsb.int.shl(4).or(lo.int)
val Int6.int get() = hsb.int.shl(5).or(lo.int)
val Int7.int get() = hsb.int.shl(6).or(lo.int)
val Int8.int get() = hsb.int.shl(7).or(lo.int)
val Int9.int get() = hsb.int.shl(8).or(lo.int)
val Int10.int get() = hsb.int.shl(9).or(lo.int)
val Int11.int get() = hsb.int.shl(10).or(lo.int)
val Int12.int get() = hsb.int.shl(11).or(lo.int)
val Int13.int get() = hsb.int.shl(12).or(lo.int)
val Int14.int get() = hsb.int.shl(13).or(lo.int)
val Int15.int get() = hsb.int.shl(14).or(lo.int)
val Int16.int get() = hsb.int.shl(15).or(lo.int)

val Int.wrappingInt0 get() = int0
val Int.wrappingInt1 get() = and(1.shl(0)).bit.lo(wrappingInt0)
val Int.wrappingInt2 get() = and(1.shl(1)).bit.lo(wrappingInt1)
val Int.wrappingInt3 get() = and(1.shl(2)).bit.lo(wrappingInt2)
val Int.wrappingInt4 get() = and(1.shl(3)).bit.lo(wrappingInt3)
val Int.wrappingInt5 get() = and(1.shl(4)).bit.lo(wrappingInt4)
val Int.wrappingInt6 get() = and(1.shl(5)).bit.lo(wrappingInt5)
val Int.wrappingInt7 get() = and(1.shl(6)).bit.lo(wrappingInt6)
val Int.wrappingInt8 get() = and(1.shl(7)).bit.lo(wrappingInt7)
val Int.wrappingInt9 get() = and(1.shl(8)).bit.lo(wrappingInt7)
val Int.wrappingInt10 get() = and(1.shl(9)).bit.lo(wrappingInt7)
val Int.wrappingInt11 get() = and(1.shl(10)).bit.lo(wrappingInt7)
val Int.wrappingInt12 get() = and(1.shl(11)).bit.lo(wrappingInt7)
val Int.wrappingInt13 get() = and(1.shl(12)).bit.lo(wrappingInt7)
val Int.wrappingInt14 get() = and(1.shl(13)).bit.lo(wrappingInt7)
val Int.wrappingInt15 get() = and(1.shl(14)).bit.lo(wrappingInt7)
val Int.wrappingInt16 get() = and(1.shl(15)).bit.lo(wrappingInt7)

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
val Int6.incOrNull get() = incOrNull(hsb, lo.incOrNull, zeroInt5) { lo(it) }
val Int7.incOrNull get() = incOrNull(hsb, lo.incOrNull, zeroInt6) { lo(it) }
val Int8.incOrNull get() = incOrNull(hsb, lo.incOrNull, zeroInt7) { lo(it) }
val Int9.incOrNull get() = incOrNull(hsb, lo.incOrNull, zeroInt8) { lo(it) }
val Int10.incOrNull get() = incOrNull(hsb, lo.incOrNull, zeroInt9) { lo(it) }
val Int11.incOrNull get() = incOrNull(hsb, lo.incOrNull, zeroInt10) { lo(it) }
val Int12.incOrNull get() = incOrNull(hsb, lo.incOrNull, zeroInt11) { lo(it) }
val Int13.incOrNull get() = incOrNull(hsb, lo.incOrNull, zeroInt12) { lo(it) }
val Int14.incOrNull get() = incOrNull(hsb, lo.incOrNull, zeroInt13) { lo(it) }
val Int15.incOrNull get() = incOrNull(hsb, lo.incOrNull, zeroInt14) { lo(it) }
val Int16.incOrNull get() = incOrNull(hsb, lo.incOrNull, zeroInt15) { lo(it) }

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
fun <R> R.forEachInt6(fn: R.(Int6) -> R): R = forEach(zeroInt6, { incOrNull }, fn)
fun <R> R.forEachInt7(fn: R.(Int7) -> R): R = forEach(zeroInt7, { incOrNull }, fn)
fun <R> R.forEachInt8(fn: R.(Int8) -> R): R = forEach(zeroInt8, { incOrNull }, fn)
fun <R> R.forEachInt9(fn: R.(Int9) -> R): R = forEach(zeroInt9, { incOrNull }, fn)
fun <R> R.forEachInt10(fn: R.(Int10) -> R): R = forEach(zeroInt10, { incOrNull }, fn)
fun <R> R.forEachInt11(fn: R.(Int11) -> R): R = forEach(zeroInt11, { incOrNull }, fn)
fun <R> R.forEachInt12(fn: R.(Int12) -> R): R = forEach(zeroInt12, { incOrNull }, fn)
fun <R> R.forEachInt13(fn: R.(Int13) -> R): R = forEach(zeroInt13, { incOrNull }, fn)
fun <R> R.forEachInt14(fn: R.(Int14) -> R): R = forEach(zeroInt14, { incOrNull }, fn)
fun <R> R.forEachInt15(fn: R.(Int15) -> R): R = forEach(zeroInt15, { incOrNull }, fn)
fun <R> R.forEachInt16(fn: R.(Int16) -> R): R = forEach(zeroInt16, { incOrNull }, fn)

val Int0.bits get() = emptySeq<Bit>()
val Int1.bits get() = Seq { hsb.thenSeqNode(lo.bits) }
val Int2.bits get() = Seq { hsb.thenSeqNode(lo.bits) }
val Int3.bits get() = Seq { hsb.thenSeqNode(lo.bits) }
val Int4.bits get() = Seq { hsb.thenSeqNode(lo.bits) }
val Int5.bits get() = Seq { hsb.thenSeqNode(lo.bits) }
val Int6.bits get() = Seq { hsb.thenSeqNode(lo.bits) }
val Int7.bits get() = Seq { hsb.thenSeqNode(lo.bits) }
val Int8.bits get() = Seq { hsb.thenSeqNode(lo.bits) }
val Int9.bits get() = Seq { hsb.thenSeqNode(lo.bits) }
val Int10.bits get() = Seq { hsb.thenSeqNode(lo.bits) }
val Int11.bits get() = Seq { hsb.thenSeqNode(lo.bits) }
val Int12.bits get() = Seq { hsb.thenSeqNode(lo.bits) }
val Int13.bits get() = Seq { hsb.thenSeqNode(lo.bits) }
val Int14.bits get() = Seq { hsb.thenSeqNode(lo.bits) }
val Int15.bits get() = Seq { hsb.thenSeqNode(lo.bits) }
val Int16.bits get() = Seq { hsb.thenSeqNode(lo.bits) }

