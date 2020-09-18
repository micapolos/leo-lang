package vm.c

import vm.ArrayType
import vm.ChoiceType
import vm.Field
import vm.IndexType
import vm.NativeType
import vm.StructType
import vm.Type

val Any.nativeTypeCode
	get() =
		when (this) {
			Unit::class.java -> "void"
			Integer::class.java -> "int"
			java.lang.Float::class.java -> "float"
			String::class.java -> "char *"
			else -> error("$this.nativeTypeCode")
		}

val Type.code: String
	get() =
		when (this) {
			is NativeType -> native.nativeTypeCode
			is IndexType -> "int"
			is StructType -> "struct { ${fields.code}}"
			is ChoiceType -> "struct { int i; union { ${fields.code}} }"
			is ArrayType -> "${elementType.code}[$size]"
		}

val Field.code: String
	get() =
		"${type.code} $name"

val List<Field>.code: String
	get() =
		joinToString("") { field -> "${field.code}; " }
