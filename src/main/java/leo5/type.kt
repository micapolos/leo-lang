package leo5

import leo.base.Empty
import leo5.core.Value as CoreValue

sealed class Type

data class EmptyType(val empty: Empty) : Type()
data class ArrayType(val array: Array) : Type()
data class StructType(val struct: Struct) : Type()
data class SwitchType(val switch: Switch) : Type()
data class FunctionType(val function: TypeFunction) : Type()

fun type(empty: Empty): Type = EmptyType(empty)
fun type(array: Array): Type = ArrayType(array)
fun type(struct: Struct): Type = StructType(struct)
fun type(switch: Switch): Type = SwitchType(switch)
fun type(function: TypeFunction): Type = FunctionType(function)

fun Type.contains(value: Value): Boolean = when (this) {
	is EmptyType -> value.isEmpty
	is ArrayType -> array.contains(value)
	is StructType -> struct.contains(value)
	is SwitchType -> switch.contains(value)
	is FunctionType -> value is FunctionValue && value.function.type == function.type
}
