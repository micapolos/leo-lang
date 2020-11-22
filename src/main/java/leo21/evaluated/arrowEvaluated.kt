package leo21.evaluated

import leo14.lambda.value.Value
import leo14.lambda.value.apply
import leo14.lambda.value.function
import leo21.prim.Prim
import leo21.prim.runtime.apply
import leo21.type.Arrow

data class ArrowEvaluated(val value: Value<Prim>, val arrow: Arrow)

infix fun Value<Prim>.of(arrow: Arrow) = ArrowEvaluated(this, arrow)

fun ArrowEvaluated.apply(evaluated: Evaluated): Evaluated =
	if (evaluated.type != arrow.lhs) null!!
	else value.function.apply(evaluated.value, Prim::apply).of(arrow.rhs)