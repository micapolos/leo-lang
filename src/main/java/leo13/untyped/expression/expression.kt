package leo13.untyped.expression

import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.expressionName
import leo9.Stack
import leo9.map

data class Expression(val opStack: Stack<Op>)

val Expression.scriptLine
	get() =
		expressionName lineTo bodyScript.emptyIfEmpty

val Expression.bodyScript
	get() =
		opStack.map { bodyScriptLine }.script