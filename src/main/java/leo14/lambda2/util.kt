package leo14.lambda2

val nil = value(null)
val const = fn(fn(at(0)(at(1))))
val id = fn(at(0))
val first = fn(fn(at(1)))
val second = fn(fn(at(0)))
val pair = fn(fn(fn(at(0)(at(2))(at(1)))))

fun Term.valueApply(valueFn: Any?.() -> Any?): Term =
	if (this is ValueTerm) value.valueFn().valueTerm
	else fn { value(it.value.valueFn()) }(this)

fun Term.valueApply(rhs: Term, f: Any?.(Any?) -> Any?): Term =
	if (this is ValueTerm && rhs is ValueTerm) value(value.f(rhs.value))
	else fn { lhs -> fn { rhs -> value(lhs.value.f(rhs.value)) } }(this)(rhs)

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