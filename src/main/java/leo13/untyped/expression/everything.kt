package leo13.untyped.expression

import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.everythingName
import leo13.untyped.scriptName

object Everything

val everything = Everything

val Everything.scriptLine
	get() =
		scriptName lineTo script(everythingName)