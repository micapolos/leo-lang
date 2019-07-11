package lambda.v2

import leo.base.fold
import leo.base.nat

fun arg0(index: Int) = term(argument(index.nat))

fun fn(fn: () -> Term) = term(function(body(fn())))

fun Arity.fn(arity: Int, termFn: Arity.() -> Term): Term =
	if (arity == 0) termFn()
	else fn { fn(arity.dec(), termFn) }

fun fn(arity: Int, termFn: Arity.() -> Term) =
	arity(arity).fn(arity, termFn)

operator fun Term.invoke(term: Term, vararg terms: Term): Term =
	apply(term).fold(terms) { apply(it) }.eval

fun Term.functionTermOrNull(arity: Int): Term? =
	if (arity == 0) this
	else functionOrNull?.body?.term?.functionTermOrNull(arity.dec())