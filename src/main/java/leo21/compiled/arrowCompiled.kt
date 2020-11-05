package leo21.compiled

import leo.base.notNullOrError
import leo14.lambda.Term
import leo14.lambda.invoke
import leo21.prim.Prim
import leo21.type.Arrow

data class ArrowCompiled(
	val term: Term<Prim>,
	val arrow: Arrow
)

infix fun Term<Prim>.of(arrow: Arrow) = ArrowCompiled(this, arrow)

fun ArrowCompiled.invokeOrNull(compiled: Compiled): Compiled? =
	if (arrow.lhs != compiled.type) null
	else Compiled(term.invoke(compiled.term), arrow.rhs)

fun ArrowCompiled.invoke(compiled: Compiled): Compiled =
	invokeOrNull(compiled).notNullOrError("type mismatch")
