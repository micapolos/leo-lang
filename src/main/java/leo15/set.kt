package leo15

import leo.base.ifOrNull

fun Typed.setOrNull(name: String, value: Typed): Typed? =
	linkOrNull?.onlyLineOrNull?.fieldOrNull?.let { field ->
		field.rhs.setLineOrNull(name, value)?.let { typed(field.typeField.name lineTo it) }
	}

// TODO: Handle setting number and text
// TODO: Handle deeper fields
// TODO: Handle setting multiple fields
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
