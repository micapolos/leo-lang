package leo32.treo

import leo.base.seq
import leo.binary.Bit

object Write

val write = Write

@Suppress("unused")
fun Write.invoke(writer: Writer, bit: Bit) =
	writer.invoke(bit)

@Suppress("unused")
val Write.char
	get() = 'w'

val Write.charSeq get() = seq(char)