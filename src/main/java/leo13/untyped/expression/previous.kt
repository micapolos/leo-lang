package leo13.untyped.expression

import leo13.script.lineTo
import leo13.script.script
import leo13.script.scriptName
import leo13.untyped.previousName

object Previous

val previous = Previous

val Previous.scriptLine
	get() =
		scriptName lineTo script(previousName)