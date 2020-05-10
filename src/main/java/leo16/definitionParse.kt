package leo16

fun Dictionary.definitionOrNull(value: Value): Definition? =
	null
		?: isDefinitionOrNull(value)
		?: givesDefinitionOrNull(value)
		?: expandsDefinitionOrNull(value)

fun Dictionary.isDefinitionOrNull(value: Value): Definition? =
	value.constantOrNull?.definition

fun Dictionary.givesDefinitionOrNull(value: Value): Definition? =
	givesOrNull(value)?.definition

fun Dictionary.expandsDefinitionOrNull(value: Value): Definition? =
	macroOrNull(value)?.definition
