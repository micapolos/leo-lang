package vm3.value.type

import vm3.deduplicate
import vm3.dsl.type.get
import vm3.dsl.type.item
import vm3.dsl.type.struct
import vm3.getOrCompute
import vm3.type.Type
import vm3.value.Value

data class Types(
	val parameters: MutableList<Type> = mutableListOf(),
	val map: MutableMap<Value, Type> = mutableMapOf()
)

operator fun Types.get(value: Value): Type =
	if (value is Value.Argument) computeType(value)
	else map.getOrCompute(value) { computeType(value) }

fun Types.push(type: Type) {
	parameters.add(type)
}

fun Types.pop() {
	parameters.removeAt(parameters.size - 1)
}

fun <T> Types.push(type: Type, fn: () -> T): T {
	push(type)
	val result = fn()
	pop()
	return result
}

fun Types.computeType(value: Value): Type =
	when (value) {
		is Value.Argument -> parameters[parameters.size - 1 - value.depth]

		is Value.Bool -> Type.Bool
		is Value.I32 -> Type.I32
		is Value.F32 -> Type.F32

		is Value.Struct -> Type.Struct(
			value.fields.map { Type.Field(it.name, get(it.value)) })

		is Value.Array -> Type.Array(
			combine(value.items.map { get(it) }),
			value.items.size)

		is Value.ArrayAt -> get(value.lhs).item
		is Value.StructAt -> get(value.lhs)[value.name]

		// TODO: Validate
		is Value.Switch -> push(value.functions[0].param) {
			get(value.functions[0].body)
		}

		is Value.Call -> push(value.function.param) {
			get(value.function.body)
		}

		is Value.Inc ->
			when (get(value.lhs)) {
				Type.I32 -> Type.I32
				else -> null
			}
		is Value.Dec ->
			when (get(value.lhs)) {
				Type.I32 -> Type.I32
				else -> null
			}
		is Value.Plus ->
			when (get(value.lhs)) {
				Type.I32 ->
					when (get(value.lhs)) {
						Type.I32 -> Type.I32
						else -> null
					}
				Type.F32 ->
					when (get(value.lhs)) {
						Type.F32 -> Type.F32
						else -> null
					}
				else -> null
			}
		is Value.Minus ->
			when (get(value.lhs)) {
				Type.I32 ->
					when (get(value.lhs)) {
						Type.I32 -> Type.I32
						else -> null
					}
				Type.F32 ->
					when (get(value.lhs)) {
						Type.F32 -> Type.F32
						else -> null
					}
				else -> null
			}
		is Value.Times ->
			when (get(value.lhs)) {
				Type.I32 ->
					when (get(value.lhs)) {
						Type.I32 -> Type.I32
						else -> null
					}
				Type.F32 ->
					when (get(value.lhs)) {
						Type.F32 -> Type.F32
						else -> null
					}
				else -> null
			}
	} ?: error("$value.type")

fun combine(types: List<Type>): Type =
	types.deduplicate.let { types ->
		when (types.size) {
			0 -> struct()
			1 -> types[0]
			else -> Type.Choice(types)
		}
	}