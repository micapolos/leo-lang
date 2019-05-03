@file:Suppress("unused")

package leo32.treo

import leo.base.seq
import leo.binary.Bit

data class Put(val bit: Bit)

fun put(bit: Bit) = Put(bit)
fun Put.invoke(sink: Sink) = sink.put(bit)
val Put.char get() = 'w'
val Put.charSeq get() = seq(char)