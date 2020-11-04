package leo21.evaluator

import leo21.prim.evaluate.value
import leo21.typed.Typed

val Typed.evaluated: Evaluated
	get() =
		Evaluated(term.value, type)
