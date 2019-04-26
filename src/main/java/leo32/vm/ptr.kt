package leo32.vm

typealias Ptr = Int
typealias Size = Int

val Int.ptrSize get() = this shl 2
fun Ptr.ptrPlus(offset: Ptr) = plus(offset.ptrSize)
val Ptr.ptrInc get() = ptrPlus(1)
val Ptr.ptrDec get() = ptrPlus(-1)
val Ptr.ptrAlign get() = plus(3).and(0x3.inv())

fun Int.align(alignment: Int) =
	plus(alignment.dec()).and(alignment.dec().inv())
