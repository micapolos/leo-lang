package leo21.compiled

import leo14.lambda.Term
import leo14.lambda.invoke
import leo14.lambda.value.Value
import leo21.type.Arrow
import leo21.type.type

data class ArrowCompiled(
	val valueTerm: Term<Value>,
	val arrow: Arrow
)

fun ArrowCompiled.invoke(compiled: Compiled): Compiled =
	if (arrow.lhs != compiled.type) error("type mismatch")
	else Compiled(valueTerm.invoke(compiled.valueTerm), arrow.rhs)

val ArrowCompiled.compiled get() = Compiled(valueTerm, type(arrow))