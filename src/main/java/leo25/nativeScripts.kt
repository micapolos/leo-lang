package leo25

import leo14.Script
import leo14.lineTo
import leo14.script

val textAppendTextScript: Script
	get() =
		script(
			textName lineTo script(anyName),
			plusName lineTo script(
				textName lineTo script(anyName)
			)
		)

val numberAddNumberScript: Script
	get() =
		script(
			numberName lineTo script(anyName),
			plusName lineTo script(
				numberName lineTo script(anyName)
			)
		)

val numberSubtractNumberScript: Script
	get() =
		script(
			numberName lineTo script(anyName),
			minusName lineTo script(
				numberName lineTo script(anyName)
			)
		)

val numberMultiplyByNumberScript: Script
	get() =
		script(
			numberName lineTo script(anyName),
			timesName lineTo script(
				numberName lineTo script(anyName)
			)
		)
