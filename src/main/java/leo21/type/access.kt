package leo21.type

import leo.base.notNullIf
import leo13.mapFirst

fun Type.accessOrNull(name: String): Type? =
	lineStack.mapFirst { accessOrNull(name) }

fun Choice.accessOrNull(name: String): Type? =
	lineStack.mapFirst { accessOrNull(name) }

fun Line.accessOrNull(name: String): Type? =
	when (this) {
		StringLine -> null
		NumberLine -> null
		is FieldLine -> field.accessOrNull(name)
		is ArrowLine -> null
		is ChoiceLine -> choice.accessOrNull(name)
		is RecursiveLine -> recursive.accessOrNull(name)
		is RecurseLine -> null
	}

fun Field.accessOrNull(name: String): Type? =
	notNullIf(this.name == name) { rhs }

fun Recursive.accessOrNull(name: String): Type? =
	resolve.accessOrNull(name)
