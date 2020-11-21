package leo21.type

import leo13.all

val Type.isStatic: Boolean
	get() =
		lineStack.all { isStatic }

val Choice.isStatic: Boolean get() = false
val Recursive.isStatic: Boolean get() = false
val Recurse.isStatic: Boolean get() = false

val Line.isStatic: Boolean
	get() =
		when (this) {
			StringLine -> false
			NumberLine -> false
			is FieldLine -> field.isStatic
			is ChoiceLine -> false
			is ArrowLine -> arrow.isStatic
			is RecursiveLine -> false
			is RecurseLine -> false
		}

val Field.isStatic: Boolean
	get() =
		rhs.isStatic

val Arrow.isStatic: Boolean
	get() =
		rhs.isStatic