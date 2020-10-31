package leo20

import leo14.Script

val Script.eval: Script
	get() =
		emptyDictionary.pushPrelude.value(this).script