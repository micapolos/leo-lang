@file:Suppress("UNCHECKED_CAST")

package leo15.lambda

import leo.base.Parameter
import leo.base.iterate
import leo.base.parameter
import leo.base.reverseStackLink
import leo.stak.*
import leo13.*

const val useEval2 = false

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
		if (useEval2) evalThunk2 else thunk.eval1
	}

val Term.evalThunk2: Thunk
	get() = thunk.eval2

fun Thunk.evalApplication(application: Thunk): Thunk {
	val evaled = application.eval2
	val fnApplied = applyFnParameter.value.invoke(this, evaled)
	return if (fnApplied != null) fnApplied
	else {
		term as AbstractionTerm
		stak.push(evaled).thunk(term.body).eval2
	}
}

fun Thunk.evalApplications(applications: Stack<Thunk>): Thunk =
	fold(applications) { evalApplication(it) }

val Thunk.eval2: Thunk
	get() {
		val stackLink = term.applicationSeqNode.reverseStackLink
		return stak.thunk(stackLink.value).resolve2
			.evalApplications(stackLink.stack.map { stak.thunk(this) })
	}

val Thunk.resolve2: Thunk
	get() =
		when (term) {
			is ValueTerm -> this
			is AbstractionTerm -> this
			is ApplicationTerm -> this
			is IndexTerm -> stak.top(term.index)!!
		}

val Thunk.eval1: Thunk
	get() =
		when (term) {
			is ValueTerm -> this
			is AbstractionTerm -> this
			is ApplicationTerm -> stak.thunk(term.lhs).eval1.let { lhs ->
				stak.thunk(term.rhs).eval1.let { rhs ->
					lhs.apply(rhs)!!
				}
			}
			is IndexTerm -> stak.top(term.index)!!
		}

fun Thunk.apply(rhs: Thunk): Thunk? =
	applyFnParameter.value.invoke(this, rhs)?.eval1 ?: when (term) {
		is ValueTerm -> null
		is AbstractionTerm -> stak.push(rhs).thunk(term.body).eval1
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
