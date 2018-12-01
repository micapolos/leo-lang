package leo

import leo.base.*

data class BackTrace(
	val patternTermStack: Stack<Term<Pattern>>) {
	override fun toString() = reflect.string
}

val Stack<Term<Pattern>>.backTrace: BackTrace
	get() =
		BackTrace(this)

fun BackTrace?.push(patternTerm: Term<Pattern>): BackTrace =
	this?.patternTermStack.push(patternTerm).backTrace

fun backTrace(patternTerm: Term<Pattern>, vararg patternTerms: Term<Pattern>) =
	stack(patternTerm, *patternTerms).backTrace

val BackTrace.back: BackTrace?
	get() =
		patternTermStack.tail?.backTrace

val BackTrace.reflect
	get() =
		backWord fieldTo term(
			traceWord fieldTo
				patternTermStack.reverse.stream.termReflect(Pattern::reflect))