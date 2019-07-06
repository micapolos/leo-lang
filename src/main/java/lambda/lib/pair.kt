package lambda.lib

import lambda.Term
import lambda.invoke
import lambda.term

val pair = term { at0 -> term { at1 -> term { bit -> bit(at0, at1) } } }

val Term.pairAt get() = term { bit -> this(bit) }
val Term.pairAt0 get() = pairAt(zeroBit)
val Term.pairAt1 get() = pairAt(oneBit)

val at = term(Term::pairAt)
val at0 = term(Term::pairAt0)
val at1 = term(Term::pairAt1)

val Pair<Term, Term>.term get() = pair(first, second)
val Term.pair get() = pairAt0 to pairAt1

fun <T0, T1> Pair<T0, T1>.term(fn0: T0.() -> Term, fn1: T1.() -> Term) =
	(first.fn0() to second.fn1()).term

fun <T0, T1> Term.pair(fn0: Term.() -> T0, fn1: Term.() -> T1) =
	pairAt0.fn0() to pairAt1.fn1()