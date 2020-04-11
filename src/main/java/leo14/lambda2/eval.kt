@file:Suppress("UNCHECKED_CAST")

package leo14.lambda2

import leo.stak.Stak
import leo.stak.emptyStak
import leo.stak.push
import leo.stak.top

typealias ValueApply = Any?.(Term) -> Term

val defaultValueApply: ValueApply = { (this as (Any?) -> Term).invoke(it) }

data class Thunk(val stak: Stak<Thunk>, val term: Term)

fun Stak<Thunk>.thunk(term: Term): Thunk = Thunk(this, term)
val Term.thunk: Thunk get() = emptyStak<Thunk>().thunk(this)

val Term.eval: Term
	get() =
		thunk.eval.term

val Thunk.eval: Thunk
	get() =
		eval(defaultValueApply)

fun Term.eval(valueApply: ValueApply): Term =
	thunk.eval(valueApply).term

fun Thunk.eval(valueApply: ValueApply): Thunk =
	when (term) {
		is ValueTerm -> this
		is AbstractionTerm -> this
		is ApplicationTerm -> stak.thunk(term.lhs).eval.apply(stak.thunk(term.rhs).eval, valueApply)
		is IndexTerm -> stak.top(term.index)!!
	}

fun Thunk.apply(rhs: Thunk, valueApply: ValueApply): Thunk =
	when (term) {
		is ValueTerm -> term.value.valueApply(rhs.term).thunk
		is AbstractionTerm -> stak.push(rhs).thunk(term.body).eval
		is ApplicationTerm -> this
		is IndexTerm -> this
	}