package leo5.core

import leo.binary.Bit
import leo.binary.isZero

data class ValuePair(val at0: Value, val at1: Value)

fun pair(at0: Value, at1: Value) = ValuePair(at0, at1)
fun ValuePair.at(bit: Bit) = if (bit.isZero) at0 else at1
