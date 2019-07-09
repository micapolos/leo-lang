package lambda.v2

import leo.base.fold
import leo.base.nat

fun arg(index: Int) = term(argument(index.nat))

fun fn(fn: () -> Term) = term(function(fn()))

fun fn(arity: Int, termFn: () -> Term): Term =
	if (arity == 0) termFn()
	else fn { fn(arity.dec(), termFn) }

operator fun Term.invoke(term: Term, vararg terms: Term): Term =
	apply(term).fold(terms) { apply(it) }.eval

fun Term.functionTermOrNull(arity: Int): Term? =
	if (arity == 0) this
	else functionOrNull?.term?.functionTermOrNull(arity.dec())