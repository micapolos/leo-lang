package lambda.lib

import lambda.Term
import lambda.invoke
import lambda.term
import leo.base.fail
import leo.generic.*

val branch0 = pair(zero)
val branch1 = pair(one)

val Term.switch get() = term { ifZero -> term { ifOne -> at0(ifZero, ifOne)(at1) } }

val switch = term(Term::switch)

fun <R> Term.switchAny(fn0: Term.() -> R, fn1: Term.() -> R) =
	when (at0) {
		zero -> at1.fn0()
		one -> at1.fn1()
		else -> fail()
	}

val Either<Term, Term>.term
	get() =
		this.switch({ branch0(this) }, { branch1(this) })

val Term.either: Either<Term, Term>
	get() =
		at0.let { at0 ->
			when (at0) {
				zero -> either(first(at1))
				one -> either(second(at1))
				else -> fail()
			}
		}

