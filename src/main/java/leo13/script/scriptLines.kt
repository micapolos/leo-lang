package leo13.script

import leo.binary.*
import leo13.*

val Boolean.scriptLine
	get() = booleanName lineTo script(if (this) trueName else falseName)

val Bit.scriptLine
	get() =
		bitName lineTo script(if (isZero) zeroName else oneName)

val Byte.scriptLine
	get() =
		byteName lineTo script(
			"first" lineTo script(bit7.scriptLine),
			"second" lineTo script(bit6.scriptLine),
			"third" lineTo script(bit5.scriptLine),
			"fourth" lineTo script(bit4.scriptLine),
			"fifth" lineTo script(bit3.scriptLine),
			"sixth" lineTo script(bit2.scriptLine),
			"seventh" lineTo script(bit1.scriptLine),
			"eight" lineTo script(bit0.scriptLine))
