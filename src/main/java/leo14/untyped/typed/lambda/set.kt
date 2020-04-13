package leo14.untyped.typed.lambda

import leo.base.ifOrNull

fun Typed.setOrNull(name: String, value: Typed): Typed? =
	linkOrNull?.onlyLineOrNull?.fieldOrNull?.let { field ->
		field.rhs.setLineOrNull(name, value)?.let { typed(field.typeField.name lineTo it) }
	}

// TODO: Handle number and text types
fun Typed.setLineOrNull(name: String, value: Typed): Typed? =
	matchLink { line ->
		line
			.fieldOrNull
			?.let { field ->
				ifOrNull(field.typeField.name == name) {
					plus(name lineTo value)
				}
			}
			?: setLineOrNull(name, value)?.plus(line)
	}
