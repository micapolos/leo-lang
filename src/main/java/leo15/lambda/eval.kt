@file:Suppress("UNCHECKED_CAST")

package leo15.lambda

import leo.base.Parameter
import leo.base.iterate
import leo.base.parameter
import leo.base.reverseStackLink
import leo.stak.*
import leo13.*

const val useTailEval = false

typealias ApplyFn = Thunk.(Thunk) -> Thunk?

val defaultApplyFn: ApplyFn = { rhs ->
	(term as? ValueTerm)?.value.run {
		(this as? ((Thunk) -> Thunk?))?.run {
			invoke(rhs)
		}
	}
}

val applyFnParameter: Parameter<ApplyFn> = parameter(defaultApplyFn)

data class Thunk(val stak: Stak<Thunk>, val term: Term)

fun Stak<Thunk>.thunk(term: Term): Thunk = Thunk(this, term)
val Term.thunk: Thunk get() = emptyStak<Thunk>().thunk(this)

val Term.eval: Term
	get() =
		evalThunk.evaledTerm

val Term.evalThunk: Thunk
	get() = resolveLambdaVars(0).run {
		if (useTailEval) tailEvalThunk else thunk.eval
	}

val Term.tailEvalThunk: Thunk
	get() = thunk.tailEval

fun Thunk.evalApplication(application: Thunk): Thunk {
	val evaled = application.tailEval
	val fnApplied = applyFnParameter.value.invoke(this, evaled)
	return if (fnApplied != null) fnApplied
	else {
		term as AbstractionTerm
		stak.push(evaled).thunk(term.body).tailEval
	}
}

fun Thunk.evalApplications(applications: Stack<Thunk>): Thunk =
	fold(applications) { evalApplication(it) }

val Thunk.tailEval: Thunk
	get() {
		val stackLink = term.applicationSeqNode.reverseStackLink
		return stak.thunk(stackLink.value).tailResolve
			.evalApplications(stackLink.stack.map { stak.thunk(this) })
	}

val Thunk.tailResolve: Thunk
	get() =
		when (term) {
			is ValueTerm -> this
			is AbstractionTerm -> this
			is ApplicationTerm -> this
			is IndexTerm -> stak.top(term.index)!!
		}

val Thunk.eval: Thunk
	get() =
		when (term) {
			is ValueTerm -> this
			is AbstractionTerm -> this
			is ApplicationTerm -> {
				val lhs = stak.thunk(term.lhs).eval
				val rhs = stak.thunk(term.rhs).eval
				lhs.apply(rhs)!!
			}
			is IndexTerm -> stak.top(term.index)!!
		}

fun Thunk.apply(rhs: Thunk): Thunk? =
	applyFnParameter.value.invoke(this, rhs)?.eval ?: when (term) {
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
