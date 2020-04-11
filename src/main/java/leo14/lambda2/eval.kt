@file:Suppress("UNCHECKED_CAST")

package leo14.lambda2

import leo.stak.Stak
import leo.stak.emptyStak
import leo.stak.push
import leo.stak.top

typealias ApplyFn = Thunk.(Thunk) -> Thunk?

val defaultApplyFn: ApplyFn = { rhs ->
	(term as? ValueTerm)?.value.run {
		(this as? ((Term) -> Term))?.run {
			invoke(rhs.term).thunk
		}
	}
}

data class Thunk(val stak: Stak<Thunk>, val term: Term)

fun Stak<Thunk>.thunk(term: Term): Thunk = Thunk(this, term)
val Term.thunk: Thunk get() = emptyStak<Thunk>().thunk(this)

val Term.eval: Term
	get() =
		thunk.eval.term

val Thunk.eval: Thunk
	get() =
		eval(defaultApplyFn)

fun Term.eval(applyFn: ApplyFn): Term =
	thunk.eval(applyFn).term

fun Thunk.eval(applyFn: ApplyFn): Thunk =
	when (term) {
		is ValueTerm -> this
		is AbstractionTerm -> this
		is ApplicationTerm -> stak.thunk(term.lhs).eval.apply(stak.thunk(term.rhs).eval, applyFn) ?: this
		is IndexTerm -> stak.top(term.index)!!
	}

fun Thunk.apply(rhs: Thunk, applyFn: ApplyFn): Thunk? =
	applyFn(rhs) ?: apply(rhs)

fun Thunk.apply(rhs: Thunk): Thunk? =
	when (term) {
		is ValueTerm -> null
		is AbstractionTerm -> stak.push(rhs).thunk(term.body).eval
		is ApplicationTerm -> null
		is IndexTerm -> null
	}
