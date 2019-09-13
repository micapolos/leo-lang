package leo13.untyped.expression

import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.everythingName

object Everything

val everything = Everything

val Everything.scriptLine
	get() =
		everythingName lineTo script()