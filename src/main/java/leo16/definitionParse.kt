package leo16

fun Dictionary.definitionOrNull(value: Value): Definition? =
	null
		?: isDefinitionOrNull(value)
		?: doesDefinitionOrNull(value)
		?: expandsDefinitionOrNull(value)

fun Dictionary.isDefinitionOrNull(value: Value): Definition? =
	value.constantOrNull?.definition

fun Dictionary.doesDefinitionOrNull(value: Value): Definition? =
	doesOrNull(value)?.definition

fun Dictionary.expandsDefinitionOrNull(value: Value): Definition? =
	macroOrNull(value)?.definition
