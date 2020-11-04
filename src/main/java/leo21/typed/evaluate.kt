package leo21.typed

import leo21.evaluated.Evaluated
import leo21.prim.evaluate.value

val Typed.evaluated: Evaluated
	get() =
		Evaluated(term.value, type)
