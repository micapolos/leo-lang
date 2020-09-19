package vm3

import vm3.dsl.type.get
import vm3.dsl.type.item

data class Types(val map: MutableMap<Value, Type> = HashMap())

operator fun Types.get(value: Value): Type =
	map.get(value) { newType(value) }

operator fun Types.set(value: Value, type: Type) {
	map[value] = type
}

fun Types.newType(value: Value): Type =
	when (value) {
		Value.Input -> null
		is Value.Bool -> Type.Bool
		is Value.I32 -> Type.I32
		is Value.F32 -> Type.F32
		is Value.Struct -> Type.Struct(value.fields.map { Type.Struct.Field(it.name, get(it.value)) })
		is Value.Array -> Type.Array(get(value.items[0]), value.items.size)
		is Value.ArrayAt -> get(value.lhs).item
		is Value.StructAt -> get(value.lhs)[value.name]
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
	} ?: error("$value.type")
