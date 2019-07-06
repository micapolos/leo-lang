package lambda.lib

import lambda.Term
import lambda.invoke
import lambda.term
import leo.base.fail
import leo.binary.Bit
import leo.binary.bit0
import leo.binary.bit1
import leo.binary.isZero

val zero = term { x -> term { y -> x } }
val one = term { x -> term { y -> y } }

val Term.not get() = this(one, zero)
infix fun Term.and(rhs: Term) = this(rhs(zero, zero), rhs(zero, one))
infix fun Term.or(rhs: Term) = this(rhs(zero, one), rhs(one, one))
infix fun Term.xor(rhs: Term) = this(rhs(zero, one), rhs(one, zero))

val not = term(Term::not)
val and = term(Term::and)
val or = term(Term::or)
val xor = term(Term::xor)

val Bit.term get() = if (isZero) zero else one
val Term.bit
	get() = when (this) {
		zero -> bit0
		one -> bit1
		else -> fail()
	}
