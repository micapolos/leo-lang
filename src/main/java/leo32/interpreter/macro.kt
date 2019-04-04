package leo32.interpreter

import leo32.runtime.Term
import leo32.runtime.evalGet
import leo32.runtime.evalWrap

data class Macro(
	val term: Term)

fun macro(term: Term) =
	Macro(term)

val Macro.eval: Term get() =
	term.evalGet.evalWrap