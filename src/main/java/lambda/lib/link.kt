package lambda.lib

import lambda.Term

val link = pair

val Term.linkHead get() = pairAt0
val Term.linkTail get() = pairAt1

val headInLink = at0InPair
val tailInLink = at1InPair
