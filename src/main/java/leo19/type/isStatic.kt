package leo19.type

import leo13.all

val Type.isStatic: Boolean
	get() =
		when (this) {
			is StructType -> struct.isStatic
			is ChoiceType -> false
			is ArrowType -> false
		}

val Struct.isStatic get() = fieldStack.all { isStatic }
val Field.isStatic get() = type.isStatic
