package leo32.vm

import kotlin.math.max

data class Struct(
	val name: String,
	val fieldList: List<Field>,
	val fieldIndexMap: Map<String, Int>,
	val byteSize: Int,
	val byteAlignment: Int)

val structMap: HashMap<String, Struct> = HashMap()

fun struct(name: String) = structMap[name] ?: error("no struct $name")

fun type(struct: Struct) = StructType(struct) as Type

fun def(name: String, vararg fields: Field): StructType {
	var offset = 0
	var alignment = 1
	val map = HashMap<String, Int>()
	var fieldIndex = 0
	fields.forEach { field ->
		val typeAlignment = field.type.byteAlignment
		offset = offset.align(typeAlignment)
		alignment = max(alignment, typeAlignment)
		field.offset = offset
		if (map.containsKey(field.name)) error("duplicate field: ${field.name}")
		map[field.name] = fieldIndex++
		offset += field.type.byteSize
	}
	return StructType(Struct(name, fields.asList(), map, offset, alignment))
}

fun Struct.field(fieldName: String): Field =
	fieldList[fieldIndexMap[fieldName] ?: error("$name.$fieldName")]

val Struct.alignedByteSize
	get() =
		byteSize.align(byteAlignment)