package leo21.type

import leo13.all

val Type.isStatic: Boolean
	get() =
		when (this) {
			is StructType -> struct.isStatic
			is ChoiceType -> false
			is RecursiveType -> false
			is RecurseType -> false
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
			is ArrowLine -> arrow.isStatic
		}

val Field.isStatic: Boolean
	get() =
		rhs.isStatic

val Arrow.isStatic: Boolean
	get() =
		rhs.isStatic