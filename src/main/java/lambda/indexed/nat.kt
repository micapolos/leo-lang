package lambda.indexed

val zero = fn { fn { a1(id) } }
val succ = fn { fn { fn { a0(a2) } } }

val Term.succ get() = succ(this).eval

val Term.forZeroOrPred get() = this
