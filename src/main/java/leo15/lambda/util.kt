package leo15.lambda

import leo.base.indexed
import leo.base.iterate
import leo.base.notNullIf
import leo13.fold
import leo13.push
import leo13.stack
import kotlin.math.max

val nilTerm = value(null)
val constTerm = fn(fn(at(1)))
val idTerm = fn(at(0))
val firstTerm = fn(fn(at(1)))
val secondTerm = fn(fn(at(0)))
val pairTerm = fn(fn(fn(at(0)(at(2))(at(1)))))

fun choiceTerm(size: Int, index: Int, term: Term): Term =
	at(index)
		.invoke(at(size))
		.iterate(size.inc()) { fn(this) }
		.invoke(term)

fun Term.unsafeUnchoice(size: Int): IndexedValue<Term> =
	applicationOrNull!!.let { application ->
		application
			.lhs
			.iterate(size.inc()) { abstractionOrNull!!.body }
			.applicationOrNull!!
			.lhs
			.indexOrNull!! indexed application.rhs
	}

fun Term.apply(fn: Term.() -> Term): Term =
	termFn { lhs -> lhs.fn() }.invoke(this)

fun Term.apply(rhs: Term, fn: Term.(Term) -> Term): Term =
	termFn { lhs -> termFn { rhs -> lhs.fn(rhs) } }.invoke(this).invoke(rhs)

fun Term.valueApply(valueFn: Any?.() -> Any?): Term =
	termFn { value(it.value.valueFn()) }.invoke(this)

fun Term.valueApply(rhs: Term, f: Any?.(Any?) -> Any?): Term =
	termFn { lhs ->
		termFn { rhs ->
			lhs.value.f(rhs.value).valueTerm
		}
	}.invoke(this).invoke(rhs)

val Term.functionize: Term
	get() =
		fn(this)(nilTerm)

val Term.unsafeUnpair: Pair<Term, Term>
	get() =
		unsafeApplicationPair.let { pair ->
			pair.first.unsafeApplicationPair.let { pair2 ->
				pair2.second to pair.second
			}
		}

val Term.freeVariableCount: Int
	get() =
		when (this) {
			is ValueTerm -> 0
			is AbstractionTerm -> body.freeVariableCount - 1
			is ApplicationTerm -> max(lhs.freeVariableCount, rhs.freeVariableCount)
			is IndexTerm -> index + 1
		}

data class Var(val depth: Int)

fun v(depth: Int) = Var(depth).valueTerm

fun Term.resolveVars(depth: Int): Term =
	when (this) {
		is ValueTerm ->
			if (value is Var) at(depth - value.depth - 1)
			else this
		is AbstractionTerm -> fn(body.resolveVars(depth.inc()))
		is ApplicationTerm -> lhs.resolveVars(depth).invoke(rhs.resolveVars(depth))
		is IndexTerm -> this
	}

val Term.resolveVars get() = resolveVars(0)

var lambdaDepth = 0

fun lambda(f: (Term) -> Term): Term {
	lambdaDepth++
	val x = fn(f(Var(lambdaDepth - 1).valueTerm))
	lambdaDepth--
	return x
}

val Term.isPair: Boolean
	get() =
		unpairOrNull != null

val Term.unpairOrNull: Pair<Term, Term>?
	get() =
		(this as? ApplicationTerm)?.let { outerApplication ->
			(outerApplication.lhs as? ApplicationTerm)?.let { innerApplication ->
				notNullIf(innerApplication.lhs == pairTerm) {
					innerApplication.rhs to outerApplication.rhs
				}
			}
		}

val Term.unsafeApplicationPair: Pair<Term, Term>
	get() =
		(this as ApplicationTerm).lhs to rhs

fun Term.append(rhs: Term): Term =
	pairTerm(this)(rhs)

tailrec fun <R> R.unsafeFold(term: Term, fn: R.(Term) -> R): R =
	if (term == idTerm) this
	else {
		val (lhs, rhs) = term.unsafeUnpair
		fn(rhs).unsafeFold(lhs, fn)
	}

fun <R> R.unsafeFoldRight(term: Term, fn: R.(Term) -> R): R =
	fold(stack<Term>().unsafeFold(term) { push(it) }, fn)
