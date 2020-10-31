package leo14.lambda.value.eval

import leo14.lambda.Term
import leo14.lambda.value.Value

val Term<Value>.evaluate: Evaluated get() = emptyScope.evaluate(this)