@file:Suppress("UNCHECKED_CAST")

package leo15.lambda

import leo.base.Parameter
import leo.base.iterate
import leo.base.parameter
import leo.stak.*
import leo13.fold
import leo13.push
import leo13.stack
import leo14.ScriptLine
import leo14.invoke
import leo15.stackName
import leo15.string
import leo15.termName
import leo15.thunkName

typealias ApplyFn = Thunk.(Thunk) -> Thunk?

val defaultApplyFn: ApplyFn = { rhs ->
	(term as? ValueTerm)?.value.run {
		(this as? ((Thunk) -> Thunk?))?.run {
			invoke(rhs)
		}
	}
}

val applyFnParameter: Parameter<ApplyFn> = parameter(defaultApplyFn)

data class Thunk(val stak: Stak<Thunk>, val term: Term) {
	override fun toString() = reflectScriptLine.string
}

val Thunk.reflectScriptLine: ScriptLine
	get() =
		thunkName(stackName(stak.contentScript { reflectScriptLine }), termName(term.script))

fun Stak<Thunk>.thunk(term: Term): Thunk = Thunk(this, term)
val Term.thunk: Thunk get() = emptyStak<Thunk>().thunk(this)

val Term.eval: Term
	get() =
		evalThunk.evaledTerm

val Term.evalThunk: Thunk
	get() = resolveLambdaVars(0).thunk.evalTail()

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

tailrec fun Thunk.evalTail(): Thunk =
	when (term) {
		is ValueTerm -> this
		is AbstractionTerm -> this
		is ApplicationTerm -> {
			val lhs = stak.thunk(term.lhs).eval
			val rhs = stak.thunk(term.rhs).eval
			applyFnParameter.value.invoke(lhs, rhs)?.eval
				?: lhs.stak.push(rhs).thunk(lhs.term.abstractionOrNull!!.body).evalTail()
		}
		is IndexTerm -> stak.top(term.index)!!
	}

fun Thunk.apply(rhs: Thunk): Thunk? =
	applyFnParameter.value.invoke(this, rhs)?.eval ?: when (term) {
		is ValueTerm -> null
		is AbstractionTerm -> stak.push(rhs).thunk(term.body).evalTail()
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
