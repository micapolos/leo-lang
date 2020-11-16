package leo21.evaluator

import leo14.lambda.value.Value
import leo21.prim.Prim
import leo21.type.Arrow

data class ArrowEvaluated(val value: Value<Prim>, val arrow: Arrow)