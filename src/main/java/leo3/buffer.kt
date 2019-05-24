package leo3

import leo.base.*
import leo.binary.Bit

data class Buffer(val bitStackOrNull: Stack<Bit>?)

val Empty.buffer get() = Buffer(null)
fun Buffer.plus(bit: Bit) = Buffer(bitStackOrNull.push(bit))
val Buffer.bitSeq get() = bitStackOrNull?.reverse?.orEmptyIfNullSeq { seq }
