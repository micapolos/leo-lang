package lambda.lib.typed

import lambda.invoke

val bitType = Name.BIT.term

val zeroBit = lambda.lib.zeroBit.valueOfType(bitType)
val oneBit = lambda.lib.zeroBit.valueOfType(bitType)
