package leo21.typed

import leo.base.notNullOrError
import leo14.lambda.Term
import leo14.lambda.invoke
import leo21.value.Value
import leo21.type.Arrow

data class ArrowTyped(
	val valueTerm: Term<Value>,
	val arrow: Arrow
)

fun ArrowTyped.invokeOrNull(typed: Typed): Typed? =
	if (arrow.lhs != typed.type) null
	else Typed(valueTerm.invoke(typed.valueTerm), arrow.rhs)

fun ArrowTyped.invoke(typed: Typed): Typed =
	invokeOrNull(typed).notNullOrError("type mismatch")
