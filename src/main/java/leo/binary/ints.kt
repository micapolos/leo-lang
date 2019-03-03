package leo.binary

import leo.base.ifNotNull
import leo.base.ifNotNullOr
import leo.inc

object Int0
data class Int1(val hi: Int0, val lo: Bit)
data class Int2(val hi: Int1, val lo: Bit)
data class Int3(val hi: Int2, val lo: Bit)
data class Int4(val hi: Int3, val lo: Bit)
data class Int5(val hi: Int4, val lo: Bit)

val int0 = Int0
fun Int0.push(lo: Bit) = Int1(this, lo)
fun Int1.push(lo: Bit) = Int2(this, lo)
fun Int2.push(lo: Bit) = Int3(this, lo)
fun Int3.push(lo: Bit) = Int4(this, lo)
fun Int4.push(lo: Bit) = Int5(this, lo)

val zeroInt1 = int0.push(0.bit)
val zeroInt2 = zeroInt1.push(0.bit)
val zeroInt3 = zeroInt2.push(0.bit)
val zeroInt4 = zeroInt3.push(0.bit)
val zeroInt5 = zeroInt4.push(0.bit)

val Int1.int get() = lo.int
val Int2.int get() = hi.int.shl(1).or(lo.int)
val Int3.int get() = hi.int.shl(1).or(lo.int)
val Int4.int get() = hi.int.shl(1).or(lo.int)
val Int5.int get() = hi.int.shl(1).or(lo.int)

val Int.int1 get() = int0.push(and(1).bit)
val Int.int2 get() = ushr(1).int1.push(and(1).bit)
val Int.int3 get() = ushr(1).int2.push(and(1).bit)
val Int.int4 get() = ushr(1).int3.push(and(1).bit)
val Int.int5 get() = ushr(1).int4.push(and(1).bit)

fun <H : Any, T> inc(hi: H, lo: Bit, hiPushFn: H.(Bit) -> T, hiIncFn: H.() -> H?): T? =
	lo.inc.ifNotNullOr(
		{ hi.hiPushFn(it) },
		{ hi.hiIncFn().ifNotNull { it.hiPushFn(Bit.ZERO) } })

val Int1.inc get() = inc(hi, lo, { push(it) }, { int0 })
val Int2.inc get() = inc(hi, lo, { push(it) }, { inc })
val Int3.inc get() = inc(hi, lo, { push(it) }, { inc })
val Int4.inc get() = inc(hi, lo, { push(it) }, { inc })
val Int5.inc get() = inc(hi, lo, { push(it) }, { inc })
