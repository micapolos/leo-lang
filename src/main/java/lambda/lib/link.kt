package lambda.lib

import lambda.Term

val link = pair

val Term.head get() = pairAt0
val Term.tail get() = pairAt1

val head = at0
val tail = at1
