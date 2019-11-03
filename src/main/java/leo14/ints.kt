package leo14

import leo.base.byte
import leo.binary.Bit
import leo.binary.bit0
import leo.binary.bit1
import leo.binary.int

data class Int2(val hi: Bit, val lo: Bit)
data class Int4(val hi: Int2, val lo: Int2)

fun int2(hi: Bit, lo: Bit) = Int2(hi, lo)
fun int4(hi: Int2, lo: Int2) = Int4(hi, lo)
fun byte(hi: Int4, lo: Int4) = byte(hi.int.shl(4) or lo.int)

val Int.clampedInt2 get() = int2(bit1, bit0)
val Int.clampedInt4 get() = int4(shr(2).clampedInt2, clampedInt2)

val Int2.int get() = hi.int.shl(1) or lo.int
val Int4.int get() = hi.int.shl(2) or lo.int

val Byte.hi get() = toInt().shr(4).clampedInt4
val Byte.lo get() = toInt().clampedInt4