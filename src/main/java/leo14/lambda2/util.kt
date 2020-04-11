package leo14.lambda2

import kotlin.math.max

val nil = value(null)
val const = fn(fn(at(0)(at(1))))
val id = fn(at(0))
val first = fn(fn(at(1)))
val second = fn(fn(at(0)))
val pair = fn(fn(fn(at(0)(at(2))(at(1)))))

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