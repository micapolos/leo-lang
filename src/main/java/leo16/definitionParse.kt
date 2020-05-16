package leo16

fun Dictionary.definitionOrNull(value: Value): Definition? =
	null
		?: isDefinitionOrNull(value)
		?: hasDefinitionOrNull(value)
		?: doesDefinitionOrNull(value)
		?: expandsDefinitionOrNull(value)

fun isDefinitionOrNull(value: Value): Definition? =
	value.isConstantOrNull?.definition

fun hasDefinitionOrNull(value: Value): Definition? =
	value.hasConstantOrNull?.definition

fun Dictionary.doesDefinitionOrNull(value: Value): Definition? =
	doesOrNull(value)?.definition

fun Dictionary.expandsDefinitionOrNull(value: Value): Definition? =
	macroOrNull(value)?.definition
