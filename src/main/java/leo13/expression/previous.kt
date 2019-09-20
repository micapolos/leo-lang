package leo13.expression

import leo13.previousName
import leo13.script.lineTo
import leo13.script.script
import leo13.script.scriptName

object Previous

val previous = Previous

val Previous.scriptLine
	get() =
		scriptName lineTo script(previousName)