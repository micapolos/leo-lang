package leo13.pattern

import leo13.*

val bitPatternLine =
	bitName lineTo pattern(options(zeroName, oneName))

val bytePatternLine =
	byteName lineTo pattern(
		"first" lineTo pattern(bitPatternLine),
		"second" lineTo pattern(bitPatternLine),
		"third" lineTo pattern(bitPatternLine),
		"fourth" lineTo pattern(bitPatternLine),
		"fifth" lineTo pattern(bitPatternLine),
		"sixth" lineTo pattern(bitPatternLine),
		"seventh" lineTo pattern(bitPatternLine),
		"eight" lineTo pattern(bitPatternLine))

val bytesPatternLine =
	"bytes" lineTo pattern(
		options(
			emptyName lineTo pattern(),
			linkName lineTo pattern(
				item(onceRecurse.increase),
				item(bytePatternLine))))

val textPatternLine =
	textName lineTo pattern(
		"utf" lineTo pattern(
			"eight" lineTo pattern(bytesPatternLine)))
