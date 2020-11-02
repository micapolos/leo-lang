package leo21.typed

import leo21.prim.eval.evaluate

val Typed.evaluate: Typed
	get() =
		Typed(term.evaluate, type)