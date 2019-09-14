package leo13

import leo13.script.lineTo
import leo13.script.script

// TODO: ESCAPE!!!
val Char.scriptLine
	get() =
		"char" lineTo script("$this")