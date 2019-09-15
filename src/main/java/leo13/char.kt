package leo13

import leo13.script.lineTo
import leo13.script.plus
import leo13.script.script

// TODO: ESCAPE!!!
val Char.scriptLine
	get() =
		"char" lineTo
			when (this) {
				'\u0004' -> script("end").plus("of" lineTo script("transmission"))
				'(' -> script("parenthesis" lineTo script("left"))
				')' -> script("parenthesis" lineTo script("right"))
				' ' -> script("space")
				'\n' -> script("new" lineTo script("line"))
				':' -> script("colon")
				else -> script("$this")
			}

const val endOfTransmissionChar = '\u0004'
