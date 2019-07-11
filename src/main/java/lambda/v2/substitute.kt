package lambda.v2

import leo.base.Nat
import leo.base.inc

fun Argument.substitute(depth: Nat, parameter: Term): Term =
	if (nat == depth) parameter else term(this)

fun Application.substitute(depth: Nat, parameter: Term): Term =
	term(application(lhs.substitute(depth, parameter), rhs.substitute(depth, parameter)))

fun Function.substitute(depth: Nat, parameter: Term): Term =
	term(function(body(body.term.substitute(depth.inc, parameter))))

fun Quote.substitute(depth: Nat, parameter: Term): Term =
	term(this)

fun Unquote.substitute(depth: Nat, parameter: Term): Term =
	term(this)

fun Term.substitute(depth: Nat, parameter: Term): Term =
	when (this) {
		is ArgumentTerm -> argument.substitute(depth, parameter)
		is ApplicationTerm -> application.substitute(depth, parameter)
		is FunctionTerm -> function.substitute(depth, parameter)
		is QuoteTerm -> quote.substitute(depth, parameter)
		is UnquoteTerm -> unquote.substitute(depth, parameter)
	}

