package leo32.vm

data class Field(
	val name: String,
	val type: Type,
	var offset: Size)

val Field.size get() = type.byteSize

fun field(name: String, type: Type) =
	Field(name, type, 0)
