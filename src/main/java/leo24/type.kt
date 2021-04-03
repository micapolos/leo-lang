package leo24

import leo.base.fold

sealed class Type
object EmptyType : Type()
data class SentenceType(val firstType: Type, val name: String, val secondType: Type) : Type()
data class AlternativeType(val firstType: Type, val secondType: Type) : Type()
data class RecursiveType(val type: Type) : Type()
data class RecurseType(val depth: Int) : Type()
data class FunctionType(val inputType: Type, val outputType: Type) : Type()
data class NativeType(val native: Any?) : Type()
data class LambdaType(val type: Type) : Type()
data class VariableType(val depth: Int) : Type()
data class ValueType(val type: Type) : Type()

val empty: Type = EmptyType
fun Type.and(pair: Pair<String, Type>): Type = SentenceType(this, pair.first, pair.second)
fun Type.or(type: Type): Type = AlternativeType(this, type)
fun recursive(type: Type): Type = RecursiveType(type)
fun recurse(depth: Int): Type = RecurseType(depth)
fun struct(type: Type, vararg pairs: Pair<String, Type>) = type.fold(pairs, Type::and)
fun struct(vararg pairs: Pair<String, Type>) = struct(empty, *pairs)
fun either(type: Type, vararg types: Type): Type = type.fold(types) { or(it) }
fun function(pair: Pair<Type, Type>): Type = FunctionType(pair.first, pair.second)
fun native(native: Any?): Type = NativeType(native)
fun lambda(type: Type): Type = LambdaType(type)
fun variable(depth: Int): Type = VariableType(depth)
fun value(type: Type): Type = ValueType(type)

val list = lambda(
	recursive(
		either(
			struct(
				value(variable(0)),
				"list" to struct()
			),
			struct(
				recurse(0),
				"add" to variable(0)
			)
		)
	)
)

