package leo

import leo.base.Stack
import leo.base.stack

data class BackTrace(
	val patternStack: Stack<Term<Pattern>>)

val Stack<Term<Pattern>>.backTrace: BackTrace
	get() =
		BackTrace(this)

fun backTrace(patternTerm: Term<Pattern>, vararg patternTerms: Term<Pattern>) =
	stack(patternTerm, *patternTerms).backTrace

val BackTrace.back: BackTrace?
	get() =
		patternStack.pop?.backTrace
