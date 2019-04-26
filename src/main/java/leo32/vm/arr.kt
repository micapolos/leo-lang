package leo32.vm

data class Arr(
	val type: Type,
	val size: Int)

val Arr.byteSize
	get() =
		type.alignedByteSize * size

val Arr.byteAlignment
	get() =
		type.byteAlignment

fun arr(type: Type, size: Size) =
	Arr(type, size)

fun Arr.offset(index: Int) =
	type.alignedByteSize * index