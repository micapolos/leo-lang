package lambda.lib

import lambda.*

val zeroBit = term { x -> term { x } }
val oneBit = term { term { y -> y } }

val Term.bitNegate get() = invoke(oneBit, zeroBit)
val Term.bitAnd get() = term { bit -> this(bit(zeroBit, zeroBit), bit(zeroBit, oneBit)) }
val Term.bitOr get() = term { bit -> this(bit(zeroBit, oneBit), bit(oneBit, oneBit)) }
val Term.bitXor get() = term { bit -> this(bit(zeroBit, oneBit), bit(oneBit, zeroBit)) }
val Term.bitEq get() = term { bit -> this(bit(oneBit, zeroBit), bit(zeroBit, oneBit)) }

val bitNegate = ap { bitNegate }
val bitEq = ap2 { bitEq }
val bitAnd = ap2 { bitAnd }
val bitOr = ap2 { bitOr }
val bitXor = ap2 { bitXor }
