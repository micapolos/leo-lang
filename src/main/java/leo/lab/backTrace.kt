package leo.lab

import leo.base.Stack
import leo.base.push
import leo.base.stack

data class BackTrace(
	val patternScriptStack: Stack<Script<Pattern>>) {
	//override fun toString() = reflect.string
}

val Stack<Script<Pattern>>.backTrace: BackTrace
	get() =
		BackTrace(this)

fun BackTrace?.push(patternScript: Script<Pattern>): BackTrace =
	this?.patternScriptStack.push(patternScript).backTrace

fun backTrace(patternScript: Script<Pattern>, vararg patternScripts: Script<Pattern>) =
	stack(patternScript, *patternScripts).backTrace

val BackTrace.patternScript: Script<Pattern>
	get() =
		patternScriptStack.head

val BackTrace.back: BackTrace?
	get() =
		patternScriptStack.tail?.backTrace

//val BackTrace.reflect
//	get() =
//		backWord fieldTo term(
//			traceWord fieldTo
//				patternScriptStack.reverse.stream.termReflect(Pattern::reflect))