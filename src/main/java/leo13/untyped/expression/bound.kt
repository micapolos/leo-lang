package leo13.untyped.expression

import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.boundName
import leo13.untyped.previousName
import leo9.Stack
import leo9.fold

data class Bound(val previousStack: Stack<Previous>)

val Bound.scriptLine: ScriptLine
	get() =
		(boundName lineTo script()).fold(previousStack) { previousName lineTo script(this) }