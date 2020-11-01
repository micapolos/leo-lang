package leo21.compiled

import leo14.lambda.Term
import leo14.lambda.value.Value
import leo21.type.Arrow

data class ArrowCompiled(
	val valueTerm: Term<Value>,
	val arrow: Arrow
)
