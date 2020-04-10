package leo14.lambda2

val nil = value(null)
val const = fn(fn(at(0)(at(1))))
val id = fn(at(0))
val first = fn(fn(at(1)))
val second = fn(fn(at(0)))
val pair = fn(fn(fn(at(0)(at(2))(at(1)))))

fun Term.valueApply(valueFn: Any?.() -> Any?): Term =
	if (this is ValueTerm) value(valueFn(value))
	else fn { value(it.value.valueFn()) }.invoke(this)

val Term.functionize: Term
	get() =
		fn(this)(nil)