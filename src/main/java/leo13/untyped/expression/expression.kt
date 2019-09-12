package leo13.untyped.expression

import leo13.script.script
import leo9.Stack
import leo9.map

data class Expression(val opStack: Stack<Op>)

val Expression.bodyScript
	get() =
		opStack.map { bodyScriptLine }.script