package lambda.v2

import leo.base.fold
import leo.base.inc
import leo.base.nat

fun arg0(index: Int) = term(argument(index.nat))

val arg get() = argument(0.nat)
val Argument.prev get() = argument(nat.inc)
val Argument.term get() = term(this)

val a0 = arg.term
val a1 = arg.prev.term
val a2 = arg.prev.prev.term
val a3 = arg.prev.prev.prev.term
fun a(int: Int) = arg0(int)

fun fn(fn: () -> Term) = term(function(body(fn())))

fun Arity.fn(arity: Int, termFn: Arity.() -> Term): Term =
	if (arity == 0) termFn()
	else term(function(body(fn(arity.dec(), termFn))))

fun fn(arity: Int, termFn: Arity.() -> Term) =
	arity(arity).fn(arity, termFn)

operator fun Term.invoke(term: Term, vararg terms: Term): Term =
	apply(term).fold(terms) { apply(it) }

fun Term.functionTermOrNull(arity: Int): Term? =
	if (arity == 0) this
	else functionOrNull?.body?.term?.functionTermOrNull(arity.dec())
