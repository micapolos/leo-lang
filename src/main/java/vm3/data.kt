package vm3

sealed class Data {
	data class Bool(val boolean: Boolean) : Data()
	data class I32(val int: Int) : Data()
	data class F32(val float: Float) : Data()
	data class Array(val items: List<Data>) : Data()
	data class Struct(val fields: List<Field>) : Data()
	data class Field(val name: String, val value: Data)
}

val Data.type: Type
	get() =
		when (this) {
			is Data.Bool -> Type.Bool
			is Data.I32 -> Type.I32
			is Data.F32 -> Type.F32
			is Data.Array -> Type.Array(items[0].type, items.size)
			is Data.Struct -> Type.Struct(fields.map { Type.Field(it.name, it.value.type) })
		}

val Data.code: String
	get() =
		when (this) {
			is Data.Bool -> "$boolean"
			is Data.I32 -> "$int"
			is Data.F32 -> "$float"
			is Data.Array -> "[ ${items.joinToString(", ") { it.code }} ]"
			is Data.Struct -> "{ ${fields.joinToString(", ") { it.code }} }"
		}

val Data.Field.code: String
	get() =
		"$name: ${value.code}"

operator fun ByteArray.set(index: Int, data: Data) =
	when (data) {
		is Data.Bool -> set(index, data.boolean.int)
		is Data.I32 -> set(index, data.int)
		is Data.F32 -> set(index, data.float.int)
		is Data.Array -> set(index, data.items)
		is Data.Struct -> set(index, data.fields.map { it.value })
	}

operator fun ByteArray.set(index: Int, datas: List<Data>) {
	datas.fold(index) { index, data ->
		index.plus(data.type.size).also { set(index, data) }
	}
}

fun ByteArray.data(index: Int, type: Type): Data =
	when (type) {
		Type.Bool -> Data.Bool(int(index).boolean)
		Type.I32 -> Data.I32(int(index))
		Type.F32 -> Data.F32(int(index).float)
		is Type.Array -> arrayData(index, type.itemType, type.itemCount)
		is Type.Struct -> structData(index, type.fields)
		is Type.Choice -> data(index + 4, Type.Struct(listOf(type.fields[int(index)])))
	}

fun ByteArray.arrayData(index: Int, type: Type, size: Int): Data {
	var index = index
	return Data.Array(
		List(size) {
			data(index, type).also { index += type.size }
		})
}

fun ByteArray.structData(index: Int, fields: List<Type.Field>): Data {
	var index = index
	return Data.Struct(
		fields.map { field ->
			Data.Field(
				field.name,
				data(index, field.valueType))
				.also { index += field.valueType.size }
		})
}
