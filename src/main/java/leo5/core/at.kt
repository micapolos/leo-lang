package leo5.core

import leo.binary.Bit

data class At(val function: Function, val bit: Bit)

fun at(function: Function, bit: Bit) = At(function, bit)
fun At.invoke(parameter: Value) = function.invoke(parameter).at(bit)