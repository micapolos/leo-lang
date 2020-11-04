package leo21.type

import leo13.all
import leo13.any

val Type.isImpossible: Boolean
	get() =
		when (this) {
			is StructType -> struct.isImpossible
			is ChoiceType -> choice.isImpossible
		}

val Struct.isImpossible: Boolean
	get() =
		lineStack.any { isImpossible }

val Choice.isImpossible: Boolean
	get() =
		lineStack.all { isImpossible }

val Line.isImpossible: Boolean
	get() =
		when (this) {
			StringLine -> false
			DoubleLine -> false
			is FieldLine -> field.isImpossible
			is ArrowLine -> arrow.isImpossible
		}

val Field.isImpossible: Boolean
	get() =
		rhs.isImpossible

val Arrow.isImpossible: Boolean
	get() =
		rhs.isImpossible
