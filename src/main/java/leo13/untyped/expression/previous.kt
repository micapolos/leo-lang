package leo13.untyped.expression

import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.previousName

object Previous

val previous = Previous

val Previous.scriptLine
	get() =
		previousName lineTo script()