package leo14.untyped.typed.lambda

import leo.base.Seq
import leo.base.emptySeq
import leo.base.seq
import leo.base.then
import leo14.lambda2.*
import java.math.BigDecimal

fun numberBinaryTerm(fn: BigDecimal.(BigDecimal) -> BigDecimal): Term =
	fn { lhs -> fn { rhs -> (lhs.value as BigDecimal).fn(rhs.value as BigDecimal).valueTerm } }

val numberPlusNumberTerm: Term = numberBinaryTerm { plus(it) }
val numberMinusNumberTerm: Term = numberBinaryTerm { minus(it) }
val numberTimesNumberTerm: Term = numberBinaryTerm { times(it) }

fun Term.plus(term: Term): Term =
	if (this == nil) term
	else pair.invoke(this).invoke(term)

val Term.repeatingTermSeq: Seq<Term>
	get() =
		seq {
			if (this == nil) null
			else unpairOrNull
				?.run { second then first.repeatingTermSeq }
				?: this then emptySeq()
		}

fun Term.apply(fn: Term.() -> Term): Term =
	fn { it.fn() }.invoke(this)