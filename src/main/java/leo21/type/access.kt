package leo21.type

import leo.base.notNullIf
import leo13.mapFirst

fun Type.accessOrNull(name: String): Type? =
	when (this) {
		is StructType -> struct.accessOrNull(name)
		is ChoiceType -> choice.accessOrNull(name)
		is RecursiveType -> recursive.accessOrNull(name)
		is RecurseType -> null
	}

fun Struct.accessOrNull(name: String): Type? =
	lineStack.mapFirst { accessOrNull(name) }

fun Choice.accessOrNull(name: String): Type? =
	lineStack.mapFirst { accessOrNull(name) }

fun Line.accessOrNull(name: String): Type? =
	fieldOrNull?.accessOrNull(name)

fun Field.accessOrNull(name: String): Type? =
	notNullIf(this.name == name) { rhs }

fun Recursive.accessOrNull(name: String): Type? =
	resolve.accessOrNull(name)
