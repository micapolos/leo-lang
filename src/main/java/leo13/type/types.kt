package leo13.type

import leo13.*

val booleanTypeLine =
	booleanName lineTo type(options(falseName, trueName))

val bitTypeLine =
	bitName lineTo type(options(zeroName, oneName))

val byteTypeLine =
	byteName lineTo type(
		"first" lineTo type(bitTypeLine),
		"second" lineTo type(bitTypeLine),
		"third" lineTo type(bitTypeLine),
		"fourth" lineTo type(bitTypeLine),
		"fifth" lineTo type(bitTypeLine),
		"sixth" lineTo type(bitTypeLine),
		"seventh" lineTo type(bitTypeLine),
		"eight" lineTo type(bitTypeLine))

val bytesTypeLine =
	"bytes" lineTo type(
		options(
			emptyName lineTo type(),
			linkName lineTo type(
				item(onceRecurse.increase),
				item(byteTypeLine))))

val textTypeLine =
	textName lineTo type(
		"utf" lineTo type(
			"eight" lineTo type(bytesTypeLine)))
