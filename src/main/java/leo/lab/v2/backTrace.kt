package leo.lab.v2

import leo.base.Stack
import leo.base.push
import leo.base.stack

data class BackTrace(
	val patternStack: Stack<Pattern>) {
	//override fun toString() = reflect.string
}

val Stack<Pattern>.backTrace: BackTrace
	get() =
		BackTrace(this)

fun BackTrace?.push(pattern: Pattern): BackTrace =
	this?.patternStack.push(pattern).backTrace

fun backTrace(pattern: Pattern, vararg patterns: Pattern) =
	stack(pattern, *patterns).backTrace

val BackTrace.pattern: Pattern
	get() =
		patternStack.head

val BackTrace.back: BackTrace?
	get() =
		patternStack.tail?.backTrace

//val BackTrace.reflect
//	get() =
//		backWord fieldTo term(
//			traceWord fieldTo
//				patternScriptStack.reverse.stream.termReflect(Pattern::reflect))