package lambda.lib.typed

import lambda.Term
import lambda.invoke
import lambda.lib.pair
import lambda.lib.pairAt0
import lambda.lib.pairAt1

val Term.valueOfType get() = pair(this)
val Term.typedValue get() = pairAt0
val Term.typedType get() = pairAt1
