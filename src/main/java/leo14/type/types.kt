package leo14.type

import leo14.Dictionary

val Dictionary.bitField
	get() =
		bit fieldTo type(choice(zero, one))

val Dictionary.byteField
	get() =
		byte fieldTo type(
			bitField,
			bitField,
			bitField,
			bitField,
			bitField,
			bitField,
			bitField,
			bitField)

fun Dictionary.listLine(itemType: Type) =
	"list" fieldTo type(
		choice(
			"empty" fieldTo type(),
			"link" fieldTo type(
				"previous" fieldTo reference(2),
				"last" fieldTo reference(itemType))))

val Dictionary.typeField: Field
	get() =
		"type" fieldTo type(
			choice(
				nativeField,
				structureField,
				choiceField,
				actionField,
				recursiveField))

val Dictionary.nativeField
	get() =
		"native" fieldTo type()

val Dictionary.structureField
	get() =
		"structure" fieldTo type(
			list(fieldField))

val Dictionary.choiceField
	get() =
		"choice" fieldTo type(
			list(fieldField))

val Dictionary.actionField
	get() =
		"action" fieldTo type(
			"start" fieldTo type(),
			"end" fieldTo type())

val Dictionary.recursiveField
	get() =
		"recursive" fieldTo reference(2)

val Dictionary.fieldField
	get() =
		"field" fieldTo type(nameLine, referenceLine)

val Dictionary.nameLine
	get() =
		"name" fieldTo type("string")

val Dictionary.referenceLine
	get() =
		"reference" fieldTo type(
			choice(
				typeField,
				"recursive" fieldTo reference(3)))
