package leo5.type

import leo.base.Empty
import leo5.Value

sealed class Type

data class EmptyType(val empty: Empty) : Type()
data class IntType(val int: TypeInt) : Type()
data class FieldType(val field: Field) : Type()
data class ApplicationType(val application: TypeApplication) : Type()
data class StructType(val struct: TypeStruct) : Type()
data class ArrayType(val array: TypeArray) : Type()
data class FunctionType(val function: TypeFunction) : Type()
data class OneOfType(val oneOf: OneOf) : Type()

fun type(empty: Empty): Type = EmptyType(empty)
fun type(int: TypeInt): Type = IntType(int)
fun type(field: Field): Type = FieldType(field)
fun type(application: TypeApplication): Type = ApplicationType(application)
fun type(struct: TypeStruct): Type = StructType(struct)
fun type(array: TypeArray): Type = ArrayType(array)
fun type(function: TypeFunction): Type = FunctionType(function)
fun type(oneOf: OneOf): Type = OneOfType(oneOf)

fun Type.compile(value: Value): Any = when (this) {
	is EmptyType -> empty.compile(value)
	is IntType -> int.compile(value)
	is FieldType -> field.compile(value)
	is ApplicationType -> application.compile(value)
	is StructType -> struct.compile(value)
	is ArrayType -> array.compile(value)
	is FunctionType -> function.compile(value)
	is OneOfType -> oneOf.compile(value)
}