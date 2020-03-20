package leo14.untyped

import leo14.lineTo
import leo14.script

val saveAsGivenScript
	get() =
		script(
			"make" lineTo script("given"),
			"save" lineTo script(
				"as" lineTo script("given")))