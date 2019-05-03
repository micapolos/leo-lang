package leo32.treo

sealed class Value
data class ConstantValue(val constant: Constant) : Value()
data class VariableValue(val variable: Variable) : Value()

fun value(constant: Constant) = ConstantValue(constant)
fun value(variable: Variable) = VariableValue(variable)
