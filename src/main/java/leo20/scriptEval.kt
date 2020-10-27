package leo20

import leo14.Script

val Script.eval: Script
	get() =
		emptyScope.value(this).script