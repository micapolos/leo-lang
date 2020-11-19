package leo21.type

import leo13.all
import leo13.any

val Type.isUninhabited: Boolean
	get() =
		when (this) {
			is StructType -> struct.isUninhabited
			is ChoiceType -> choice.isUninhabited
			is RecursiveType -> false
			is RecurseType -> false
		}

val Struct.isUninhabited: Boolean
	get() =
		lineStack.any { isUninhabited }

val Choice.isUninhabited: Boolean
	get() =
		lineStack.all { isUninhabited }

val Line.isUninhabited: Boolean
	get() =
		when (this) {
			StringLine -> false
			NumberLine -> false
			is FieldLine -> field.isUninhabited
			is ArrowLine -> arrow.isUninhabited
		}

val Field.isUninhabited: Boolean
	get() =
		rhs.isUninhabited

val Arrow.isUninhabited: Boolean
	get() =
		rhs.isUninhabited
