package leo.lab

import leo.base.*

data class OneOf(
	val patternScriptStack: Stack<Script<Pattern>>)

fun oneOf(script: Script<Pattern>, vararg scripts: Script<Pattern>): OneOf =
	OneOf(stack(script, *scripts))

fun OneOf.or(script: Script<Pattern>): OneOf =
	OneOf(patternScriptStack.push(script))

val OneOf.patternScriptStream: Stream<Script<Pattern>>
	get() =
		patternScriptStack.stream

fun Script<Nothing>.matches(oneOf: OneOf, backTraceOrNull: BackTrace?): Boolean =
	oneOf.patternScriptStream.first { oneOfScript ->
		matches(oneOfScript, backTraceOrNull)
	} != null
