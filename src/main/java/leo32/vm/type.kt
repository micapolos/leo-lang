package leo32.vm

sealed class Type
object ByteType : Type()
object IntType : Type()
object FloatType : Type()
object PtrType : Type()
data class StructType(val struct: Struct) : Type()
data class ArrType(val arr: Arr) : Type()

val byteType = ByteType
val intType = IntType
val floatType = FloatType
val ptrType = PtrType

val Type.byteSize: Int
	get() =
		when (this) {
			is ByteType -> 1
			is IntType -> 4
			is FloatType -> 4
			is PtrType -> 4
			is StructType -> struct.byteSize
			is ArrType -> arr.byteSize
		}

val Type.byteAlignment: Int
	get() =
		when (this) {
			is ByteType -> 1
			is IntType -> 4
			is FloatType -> 4
			is PtrType -> 4
			is StructType -> struct.byteAlignment
			is ArrType -> arr.byteAlignment
		}

val Type.alignedByteSize
	get() =
		byteSize.align(byteAlignment)