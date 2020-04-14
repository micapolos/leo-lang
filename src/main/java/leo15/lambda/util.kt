package leo15.lambda

import leo.base.notNullIf
import kotlin.math.max

val nil = value(null)
val const = fn(fn(at(0)(at(1))))
val id = fn(at(0))
val first = fn(fn(at(1)))
val second = fn(fn(at(0)))
val pair = fn(fn(fn(at(0)(at(2))(at(1)))))

fun Term.apply(fn: Term.() -> Term): Term =
	fn { lhs -> lhs.fn() }.invoke(this)

fun Term.apply(rhs: Term, fn: Term.(Term) -> Term): Term =
	fn { lhs -> fn { rhs -> lhs.fn(rhs) } }.invoke(this).invoke(rhs)

fun Term.valueApply(valueFn: Any?.() -> Any?): Term =
	fn { value(it.value.valueFn()) }.invoke(this)

fun Term.valueApply(rhs: Term, f: Any?.(Any?) -> Any?): Term =
	fn { lhs ->
		fn { rhs ->
			lhs.value.f(rhs.value).valueTerm
		}
	}.invoke(this).invoke(rhs)

val Term.functionize: Term
	get() =
		fn(this)(nil)

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

val Term.isPair: Boolean
	get() =
		unpairOrNull != null

val Term.unpairOrNull: Pair<Term, Term>?
	get() =
		(this as? ApplicationTerm)?.let { outerApplication ->
			(outerApplication.lhs as? ApplicationTerm)?.let { innerApplication ->
				notNullIf(innerApplication.lhs == pair) {
					innerApplication.rhs to outerApplication.rhs
				}
			}
		}

val Term.unsafeApplicationPair: Pair<Term, Term>
	get() =
		(this as ApplicationTerm).lhs to rhs