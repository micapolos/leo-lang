package leo32.treo

import leo.binary.Bit

sealed class Value
data class ConstantValue(val constant: Constant) : Value()
data class VariableValue(val variable: Variable) : Value()

fun value(constant: Constant) = ConstantValue(constant)
fun value(variable: Variable) = VariableValue(variable)

val Value.bit
	get() =
		when (this) {
			is ConstantValue -> constant.bit
			is VariableValue -> variable.bit
		}

fun Value.enter(bit: Bit): Boolean =
	when (this) {
		is ConstantValue -> constant.bit == bit
		is VariableValue -> {
			variable.bit = bit; true
		}
	}

