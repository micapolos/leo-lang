package leo.binary

import leo.base.Bit
import leo.base.bit
import leo.base.bitMaskOrNull

data class Int0 internal constructor(val int: Int)
data class Int1 internal constructor(val int: Int)
data class Int2 internal constructor(val int: Int)
data class Int3 internal constructor(val int: Int)
data class Int4 internal constructor(val int: Int)
data class Int5 internal constructor(val int: Int)
data class Int6 internal constructor(val int: Int)
data class Int7 internal constructor(val int: Int)

val Int.int0 get() = Int0(and(0.bitMaskOrNull!!))
val Int.int1 get() = Int1(and(1.bitMaskOrNull!!))
val Int.int2 get() = Int2(and(2.bitMaskOrNull!!))
val Int.int3 get() = Int3(and(3.bitMaskOrNull!!))
val Int.int4 get() = Int4(and(4.bitMaskOrNull!!))
val Int.int5 get() = Int5(and(5.bitMaskOrNull!!))
val Int.int6 get() = Int6(and(6.bitMaskOrNull!!))
val Int.int7 get() = Int7(and(7.bitMaskOrNull!!))

val Int2.bits: Iterable<Bit>
	get() = listOf(
		int.and(1.shl(1)).bit,
		int.and(1.shl(0)).bit)


