package leo21.typed

import leo14.lambda.value.eval.evaluate
import leo14.lambda.value.eval.term

val Typed.evaluate: Typed
	get() =
		Typed(valueTerm.evaluate.term, type)