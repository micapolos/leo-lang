package leo32.treo

import leo.binary.Bit

typealias WriteFn = Bit.() -> Unit

data class Writer(
	val writeFn: WriteFn)

fun Writer.write(bit: Bit) = writeFn.invoke(bit)

val nullWriter: Writer = Writer { Unit }