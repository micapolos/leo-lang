package lambda.lib.typed

import lambda.invoke

val bitType = genType

val zeroBit = lambda.lib.zeroBit.typedOf.invoke(bitType)
val oneBit = lambda.lib.oneBit.typedOf.invoke(bitType)
