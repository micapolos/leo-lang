package leo16

import leo13.Link
import leo15.lambda.Term
import leo16.names.*

data class Typed(val term: Term, val type: Type)

infix fun Term.of(type: Type) = Typed(this, type)

val Typed.value get() = type.value.value(term)

fun Value.of(type: Type) =
	type.value.termOrNull(this)
		?.of(type)
		?: throw AssertionError(plus(_of(type.value)))

