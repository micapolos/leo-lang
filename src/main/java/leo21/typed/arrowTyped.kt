package leo21.typed

import leo14.lambda.Term
import leo14.lambda.invoke
import leo14.lambda.value.Value
import leo21.type.Arrow
import leo21.type.type

data class ArrowTyped(
	val valueTerm: Term<Value>,
	val arrow: Arrow
)

fun ArrowTyped.invoke(typed: Typed): Typed =
	if (arrow.lhs != typed.type) error("type mismatch")
	else Typed(valueTerm.invoke(typed.valueTerm), arrow.rhs)

val ArrowTyped.typed get() = Typed(valueTerm, type(arrow))