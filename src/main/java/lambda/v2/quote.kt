package lambda.v2

import leo.base.Nat
import leo.base.SuccNat
import leo.base.ZeroNat
import leo.binary.Zero

object Quote

val quote = Quote

val id = fn(1) { arg(1) }

val Zero.quotedTerm get() = id

val Nat.quotedTerm: Term
	get() = when (this) {
		is ZeroNat -> fn(2) { arg(1)(zero.quotedTerm) }
		is SuccNat -> fn(2) { arg(2)(succ.nat.quotedTerm) }
	}

val Argument.quotedTerm get() = nat.quotedTerm

val Application.quotedTerm get() = fn(1) { arg(1)(lhs.quotedTerm, rhs.quotedTerm) }

val Function.quotedTerm get() = term.quotedTerm

val Quote.quotedTerm get() = id

val Unquote.quotedTerm get() = id

val Term.quotedTerm: Term
	get() =
		when (this) {
			is ArgumentTerm -> fn(5) { arg(1)(argument.quotedTerm) }
			is ApplicationTerm -> fn(5) { arg(2)(application.quotedTerm) }
			is FunctionTerm -> fn(5) { arg(3)(function.quotedTerm) }
			is QuoteTerm -> fn(5) { arg(4)(quote.quotedTerm) }
			is UnquoteTerm -> fn(5) { arg(5)(unquote.quotedTerm) }
		}

