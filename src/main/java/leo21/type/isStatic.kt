package leo21.type

import leo13.all

val Type.isStatic: Boolean
	get() =
		switch(
			Struct::isStatic,
			Choice::isStatic,
			Recursive::isStatic,
			Recurse::isStatic)

val Struct.isStatic: Boolean
	get() =
		lineStack.all { isStatic }

val Choice.isStatic: Boolean get() = false
val Recursive.isStatic: Boolean get() = false
val Recurse.isStatic: Boolean get() = false

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