package leo21.typed

import leo21.value.eval.evaluate
import leo21.value.eval.term

val Typed.evaluate: Typed
	get() =
		Typed(valueTerm.evaluate.term, type)