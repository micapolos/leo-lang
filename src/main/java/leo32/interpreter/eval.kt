package leo32.interpreter

import leo.base.empty
import leo.base.fold
import leo32.runtime.Term
import leo32.runtime.TermField

fun eval(vararg fields: TermField) =
	empty.evaluator.fold(fields) { eval(it) }

fun eval(term: Term) =
	empty.evaluator.eval(term).term