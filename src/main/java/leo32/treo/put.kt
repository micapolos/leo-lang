package leo32.treo

import leo.base.seq
import leo.binary.Bit

object Put

val put = Put

@Suppress("unused")
fun Put.invoke(sink: Sink, bit: Bit) =
	sink.put(bit)

@Suppress("unused")
val Put.char
	get() = 'w'

val Put.charSeq get() = seq(char)