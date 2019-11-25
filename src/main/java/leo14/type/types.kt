package leo14.type

import leo13.index
import leo14.Dictionary

val Dictionary.bitField
	get() =
		bit fieldTo type(choice(zero, one))

val Dictionary.byteField
	get() =
		byte fieldTo type(
			first fieldTo type(bitField),
			second fieldTo type(bitField),
			third fieldTo type(bitField),
			fourth fieldTo type(bitField),
			fifth fieldTo type(bitField),
			sixth fieldTo type(bitField),
			seventh fieldTo type(bitField),
			eight fieldTo type(bitField))

fun Dictionary.listLine(itemType: Type) =
	"list" fieldTo type(
		choice(
			"empty" fieldTo type(),
			"link" fieldTo type(
				"previous" fieldTo reference(index(2)),
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
			"field" fieldTo type(
				listLine(
					type(fieldField))))

val Dictionary.choiceField
	get() =
		"choice" fieldTo type(
			"field" fieldTo type(
				listLine(
					type(fieldField))))

val Dictionary.actionField
	get() =
		"action" fieldTo type(
			"start" fieldTo type(),
			"end" fieldTo type())

val Dictionary.recursiveField
	get() =
		"recursive" fieldTo reference(index(2))

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
				"recursive" fieldTo reference(index(3))))
