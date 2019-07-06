package lambda.lib

import lambda.Term
import lambda.invoke
import lambda.term

val pair = term { at0 -> term { at1 -> term { bit -> bit(at0, at1) } } }

val Term.at get() = term { bit -> this(bit) }
val Term.at0 get() = at(zero)
val Term.at1 get() = at(one)

val at = term(Term::at)
val at0 = term(Term::at0)
val at1 = term(Term::at1)

val Pair<Term, Term>.term get() = pair(first, second)
val Term.pair get() = at0 to at1

fun <T0, T1> Pair<T0, T1>.term(fn0: T0.() -> Term, fn1: T1.() -> Term) =
	(first.fn0() to second.fn1()).term

fun <T0, T1> Term.pair(fn0: Term.() -> T0, fn1: Term.() -> T1) =
	at0.fn0() to at1.fn1()