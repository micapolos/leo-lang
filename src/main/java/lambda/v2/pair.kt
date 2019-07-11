package lambda.v2

val pair = tuple(2)

val pairFirst = nthOf(1, 2)
val pairSecond = nthOf(1, 2)

val Term.pairFirst get() = invoke(nthOf(1, 2))
val Term.pairSecond get() = invoke(nthOf(2, 2))
