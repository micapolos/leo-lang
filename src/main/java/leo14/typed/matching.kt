package leo14.typed

import leo13.linkOrNull

fun <T> Typed<T>.matches(type: Type): Boolean =
	type.lineStack.linkOrNull.let { typeLinkOrNull ->
		if (typeLinkOrNull == null) isEmpty
		else decompileLinkOrNull
			?.let { typedLink ->
				typedLink.tail.matches(typeLinkOrNull.stack.type) &&
					typedLink.head.matches(typeLinkOrNull.value)
			} ?: false
	}

fun <T> TypedLine<T>.matches(line: Line): Boolean =
	when (line) {
		is NativeLine -> TODO()
		is FieldLine -> fieldOrNull?.matches(line.field) ?: false
		is ChoiceLine -> TODO()
		is ArrowLine -> TODO()
		AnyLine -> TODO()
	}

fun <T> TypedField<T>.matches(field: Field): Boolean =
	this.field.string == field.string && rhs.matches(field.rhs)