package leo21.typed

import leo21.prim.evaluate.evaluate

val Typed.evaluate: Typed
	get() =
		Typed(term.evaluate, type)