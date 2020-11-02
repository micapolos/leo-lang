package leo21.value.eval

import leo14.lambda.Term
import leo21.value.Value

val Term<Value>.evaluate: Evaluated get() = emptyScope.evaluate(this)