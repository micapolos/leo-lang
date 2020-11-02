package leo21.type

import leo13.all

val Type.isStatic: Boolean
	get() =
		when (this) {
			is StructType -> struct.isStatic
			is ChoiceType -> false
		}

val Struct.isStatic: Boolean
	get() =
		lineStack.all { isStatic }

val Line.isStatic: Boolean
	get() =
		when (this) {
			StringLine -> false
			DoubleLine -> false
			is FieldLine -> field.isStatic
			is ArrowLine -> false // TODO???
		}

val Field.isStatic: Boolean
	get() =
		rhs.isStatic