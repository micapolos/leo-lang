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
		(this as? ((Thunk) -> Thunk?))?.run {
			invoke(rhs)
		}
	}
}

data class Thunk(val stak: Stak<Thunk>, val term: Term)

fun Stak<Thunk>.thunk(term: Term): Thunk = Thunk(this, term)
val Term.thunk: Thunk get() = emptyStak<Thunk>().thunk(this)

val Term.eval: Term
	get() =
		eval(defaultApplyFn)

fun Term.eval(applyFn: ApplyFn): Term =
	resolveLambdaVars(0).thunk.eval(applyFn).evaledTerm

fun Thunk.eval(applyFn: ApplyFn): Thunk =
	when (term) {
		is ValueTerm -> this
		is AbstractionTerm -> this
		is ApplicationTerm -> stak.thunk(term.lhs).eval(applyFn).let { lhs ->
			stak.thunk(term.rhs).eval(applyFn).let { rhs ->
				lhs.apply(rhs, applyFn)!!
			}
		}
		is IndexTerm -> stak.top(term.index)!!
		is RepeatTerm -> stak.thunk(term.rhs).eval(applyFn).let { evaled ->
			evaled.copy(term = evaled.term.repeat)
		}
	}

tailrec fun Thunk.apply(rhs: Thunk, applyFn: ApplyFn): Thunk? =
	applyFn(rhs)?.eval(applyFn) ?: when (term) {
		is ValueTerm -> null
		is AbstractionTerm -> {
			val evaled = stak.push(rhs).thunk(term.body).eval(applyFn)
			if (term.isRepeating && evaled.term is RepeatTerm)
				apply(evaled.copy(term = evaled.term.rhs), applyFn)
			else evaled
		}
		is ApplicationTerm -> null
		is IndexTerm -> null
		is RepeatTerm -> null
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
