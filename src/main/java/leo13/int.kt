package leo13

import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

// TODO: Encode as digit list!!!
val Int.scriptLine: ScriptLine
	get() =
		"$this" lineTo script()