package leo.term

import leo.base.Stack
import leo.base.onlyStack
import leo.base.push

data class Trace(
	val patternStack: Stack<Pattern>)

val Stack<Pattern>.trace: Trace
	get() =
		Trace(this)

val Pattern.startTrace: Trace
	get() =
		onlyStack.trace

fun Trace?.plus(pattern: Pattern): Trace =
	this?.patternStack.push(pattern).trace

val Trace.back: Trace?
	get() =
		patternStack.tail?.trace

val Trace.lastPattern: Pattern
	get() =
		patternStack.head