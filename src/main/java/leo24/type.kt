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

val empty: Type = EmptyType
fun Type.and(pair: Pair<String, Type>): Type = SentenceType(this, pair.first, pair.second)
fun Type.or(type: Type): Type = AlternativeType(this, type)
fun recursive(type: Type): Type = RecursiveType(type)
fun recurse(depth: Int): Type = RecurseType(depth)
fun struct(vararg pairs: Pair<String, Type>) = empty.fold(pairs, Type::and)
fun either(type: Type, vararg types: Type): Type = type.fold(types) { or(it) }
fun function(pair: Pair<Type, Type>): Type = FunctionType(pair.first, pair.second)
fun native(native: Any?): Type = NativeType(native)

val x = struct(
	"circle" to struct(
		"radius" to either(
			struct("zero" to struct()),
			struct("one" to struct())
		),
		"center" to struct(
			"point" to struct(
				"x" to recursive(recurse(0)),
				"y" to struct()
			)
		)
	),
	"map" to function(struct() to struct())
)
