package leo21.typed

import leo.base.notNullOrError
import leo14.lambda.Term
import leo14.lambda.invoke
import leo21.prim.Prim
import leo21.type.Arrow

data class ArrowTyped(
	val term: Term<Prim>,
	val arrow: Arrow
)

infix fun Term<Prim>.of(arrow: Arrow) = ArrowTyped(this, arrow)

fun ArrowTyped.invokeOrNull(typed: Typed): Typed? =
	if (arrow.lhs != typed.type) null
	else Typed(term.invoke(typed.term), arrow.rhs)

fun ArrowTyped.invoke(typed: Typed): Typed =
	invokeOrNull(typed).notNullOrError("type mismatch")
