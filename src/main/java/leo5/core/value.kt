package leo5.core

import leo.base.Empty
import leo.binary.Bit
import leo.binary.bit0
import leo.binary.bit1

sealed class Value

data class EmptyValue(val empty: Empty) : Value()
data class BitValue(val bit: Bit) : Value()
data class PairValue(val pair: ValuePair) : Value()
data class FunctionValue(val function: Function) : Value()

fun value(empty: Empty): Value = EmptyValue(empty)
fun value(bit: Bit): Value = BitValue(bit)
fun value(pair: ValuePair) = PairValue(pair)
fun value(function: Function): Value = FunctionValue(function)

val Value.isEmpty get() = (this is EmptyValue)
val Value.bit get() = (this as BitValue).bit
fun Value.at(bit: Bit) = (this as PairValue).pair.at(bit)
val Value.at0 get() = at(bit0)
val Value.at1 get() = at(bit1)
fun Value.invoke(argument: Value) = (this as FunctionValue).function.invoke(argument)
