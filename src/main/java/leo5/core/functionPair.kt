package leo5.core

import leo.binary.Bit
import leo.binary.isZero

data class FunctionPair(val at0: Function, val at1: Function)

fun pair(at0: Function, at1: Function) = FunctionPair(at0, at1)
fun FunctionPair.at(bit: Bit) = if (bit.isZero) at0 else at1
fun FunctionPair.invoke(parameter: Value) = value(pair(at0.invoke(parameter), at1.invoke(parameter)))