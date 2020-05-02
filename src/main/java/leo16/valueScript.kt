package leo16

import leo13.array
import leo13.map
import leo14.Script
import leo14.ScriptLine
import leo14.lineTo
import leo14.script

val Value.script: Script
	get() =
		script(*fieldStack.map { scriptLine }.array)

val Field.scriptLine: ScriptLine
	get() =
		printValueSentence.scriptLine

val ValueSentence.scriptLine: ScriptLine
	get() =
		word lineTo value.script