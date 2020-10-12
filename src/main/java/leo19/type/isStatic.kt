package leo19.type

import leo13.all
import leo13.filter
import leo13.linkOrNull

val Type.isStatic: Boolean
	get() =
		when (this) {
			is StructType -> struct.isStatic
			is ChoiceType -> false
			is ArrowType -> false
		}

val Struct.isStatic get() = fieldStack.all { isStatic }
val Field.isStatic get() = type.isStatic

val Struct.isComplex get() = fieldStack.filter { !isStatic }.linkOrNull?.stack?.linkOrNull != null
val Choice.isSimple get() = caseStack.all { type.isStatic }