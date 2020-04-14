@file:Suppress("UNCHECKED_CAST")

package leo15.lambda

import leo.base.iterate
import leo.stak.*
import leo13.fold
import leo13.push
import leo13.stack

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
		thunk.eval.evaledTerm

val Thunk.eval: Thunk
	get() =
		eval(defaultApplyFn)

fun Term.eval(applyFn: ApplyFn): Term =
	thunk.eval(applyFn).term

fun Thunk.eval(applyFn: ApplyFn): Thunk =
	when (term) {
		is ValueTerm -> this
		is AbstractionTerm -> this
		is ApplicationTerm -> stak.thunk(term.lhs).eval.let { lhs ->
			stak.thunk(term.rhs).eval.let { rhs ->
				lhs.apply(rhs, applyFn) ?: emptyStak<Thunk>().thunk(lhs.term(rhs.term))
			}
		}
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

val Thunk.evaledTerm: Term
	get() =
		term.freeVariableCount.let { count ->
			(stak to stack<Thunk>()).iterate(count) {
				first.unlink!!.let { pair ->
					pair.first to second.push(pair.second)
				}
			}.second.let { stack ->
				term.iterate(count) { fn(this) }.fold(stack) { invoke(it.evaledTerm) }
			}
		}
