package leo5.core

import leo.binary.Bit

sealed class Value

data class BitValue(val bit: Bit) : Value()
data class PairValue(val pair: ValuePair) : Value()
data class FunctionValue(val function: Function) : Value()

fun value(bit: Bit): Value = BitValue(bit)
fun value(pair: ValuePair) = PairValue(pair)
fun value(function: Function): Value = FunctionValue(function)

val Value.bit get() = (this as BitValue).bit
fun Value.at(bit: Bit) = (this as PairValue).pair.at(bit)
fun Value.invoke(argument: Value) = (this as FunctionValue).function.invoke(argument)
