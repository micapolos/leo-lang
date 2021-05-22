package leo25

import leo14.Script
import leo14.lineTo
import leo14.script

val textAppendTextScript: Script
	get() =
		script(
			textName lineTo script(anyName),
			appendName lineTo script(
				textName lineTo script(anyName)
			)
		)

val numberAddNumberScript: Script
	get() =
		script(
			numberName lineTo script(anyName),
			addName lineTo script(
				numberName lineTo script(anyName)
			)
		)

val numberSubtractNumberScript: Script
	get() =
		script(
			numberName lineTo script(anyName),
			subtractName lineTo script(
				numberName lineTo script(anyName)
			)
		)

val numberMultiplyByNumberScript: Script
	get() =
		script(
			numberName lineTo script(anyName),
			multiplyName lineTo script(
				byName lineTo script(
					numberName lineTo script(anyName)
				)
			)
		)

val getHashScript: Script
	get() =
		script(
			anyName lineTo script(),
			getName lineTo script(hashName)
		)

val anyIsAnyScript: Script
	get() =
		script(
			anyName lineTo script(),
			isName lineTo script(anyName)
		)