package leo13.value

import leo.binary.*
import leo13.*

val Boolean.valueLine
	get() = booleanName lineTo value(if (this) trueName else falseName)

val Bit.valueLine
	get() =
		bitName lineTo value(if (isZero) zeroName else oneName)

val Byte.valueLine
	get() =
		byteName lineTo value(
			"first" lineTo value(bit7.valueLine),
			"second" lineTo value(bit6.valueLine),
			"third" lineTo value(bit5.valueLine),
			"fourth" lineTo value(bit4.valueLine),
			"fifth" lineTo value(bit3.valueLine),
			"sixth" lineTo value(bit2.valueLine),
			"seventh" lineTo value(bit1.valueLine),
			"eight" lineTo value(bit0.valueLine))
