package vm3.data

import vm3.float
import vm3.int
import vm3.set
import vm3.type.Type
import vm3.type.size

sealed class Data {
	data class F32(val float: Float) : Data()
	data class Struct(val fields: List<Field>) : Data()
	data class Field(val name: String, val value: Data)
}

val Data.type: Type
	get() =
		when (this) {
			is Data.F32 -> Type.F32
			is Data.Struct -> Type.Struct(fields.map { Type.Field(it.name, it.value.type) })
		}

val Data.code: String
	get() =
		when (this) {
			is Data.F32 -> "$float"
			is Data.Struct -> "{ ${fields.joinToString(", ") { it.code }} }"
		}

val Data.Field.code: String
	get() =
		"$name: ${value.code}"

operator fun ByteArray.set(index: Int, data: Data) =
	when (data) {
		is Data.F32 -> set(index, data.float.int)
		is Data.Struct -> set(index, data.fields.map { it.value })
	}

operator fun ByteArray.set(index: Int, datas: List<Data>) {
	datas.fold(index) { index, data ->
		index.plus(data.type.size).also { set(index, data) }
	}
}

fun ByteArray.data(index: Int, type: Type): Data =
	when (type) {
		Type.F32 -> Data.F32(int(index).float)
		is Type.Struct -> structData(index, type.fields)
		is Type.Choice -> data(index + 4, type.caseTypes[int(index)])
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
