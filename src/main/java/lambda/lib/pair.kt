package lambda.lib

import lambda.Term
import lambda.invoke
import lambda.term

val pair = term { at0 -> term { at1 -> term { bit -> bit(at0, at1) } } }

val Term.pairTo get() = pair(this)
val Term.pairAt get() = term { bit -> this(bit) }
val Term.pairAt0 get() = pairAt(zeroBit)
val Term.pairAt1 get() = pairAt(oneBit)

val at = term(Term::pairAt)
val at0 = term(Term::pairAt0)
val at1 = term(Term::pairAt1)
