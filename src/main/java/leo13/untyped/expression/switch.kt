package leo13.untyped.expression

import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.switchName
import leo9.Stack
import leo9.map

data class Switch(val caseStack: Stack<Case>)

val Switch.scriptLine
	get() =
		switchName lineTo caseStack.map { scriptLine }.script

