package lambda.v2

import leo.base.Nat
import leo.base.SuccNat
import leo.base.ZeroNat
import leo.binary.Zero

object Quote

val quote = Quote

val id = fn { arg(0) }

val Zero.quotedTerm get() = id

val Nat.quotedTerm: Term
	get() = when (this) {
		is ZeroNat -> fn { fn { arg(0)(zero.quotedTerm) } }
		is SuccNat -> fn { fn { arg(1)(succ.nat.quotedTerm) } }
	}

val Argument.quotedTerm get() = nat.quotedTerm

val Application.quotedTerm get() = fn { arg(0)(lhs.quotedTerm, rhs.quotedTerm) }

val Function.quotedTerm get() = term.quotedTerm

val Quote.quotedTerm get() = id

val Unquote.quotedTerm get() = id

val Term.quotedTerm: Term
	get() =
		when (this) {
			is ArgumentTerm -> fn(5) { arg(0)(argument.quotedTerm) }
			is ApplicationTerm -> fn(5) { arg(1)(application.quotedTerm) }
			is FunctionTerm -> fn(5) { arg(2)(function.quotedTerm) }
			is QuoteTerm -> fn(5) { arg(3)(quote.quotedTerm) }
			is UnquoteTerm -> fn(5) { arg(4)(unquote.quotedTerm) }
		}

