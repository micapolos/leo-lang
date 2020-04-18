package leo15

import leo.base.Seq
import leo.base.emptySeq
import leo.base.seq
import leo.base.then
import leo15.lambda.*
import java.math.BigDecimal

fun numberBinaryTerm(fn: BigDecimal.(BigDecimal) -> BigDecimal): Term =
	leo15.lambda.termFn { lhs -> leo15.lambda.termFn { rhs -> (lhs.value as BigDecimal).fn(rhs.value as BigDecimal).valueTerm } }

val numberPlusNumberTerm: Term = numberBinaryTerm { plus(it) }
val numberMinusNumberTerm: Term = numberBinaryTerm { minus(it) }
val numberTimesNumberTerm: Term = numberBinaryTerm { times(it) }

fun Term.plus(term: Term): Term =
	if (this == nilTerm) term
	else pairTerm.invoke(this).invoke(term)

fun Term.plusRepeating(term: Term): Term =
	pairTerm.invoke(this).invoke(term)

val Term.repeatingTermSeq: Seq<Term>
	get() =
		seq {
			if (this == nilTerm) null
			else unpairOrNull
				?.run { second then first.repeatingTermSeq }
				?: this then emptySeq()
		}

fun Term.apply(fn: Term.() -> Term): Term =
	leo15.lambda.termFn { it.fn() }.invoke(this)