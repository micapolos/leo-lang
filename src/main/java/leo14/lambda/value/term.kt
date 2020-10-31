package leo14.lambda.value

import leo14.lambda.Term

val Term<Value>.eval: Value get() = emptyScope.eval(this)