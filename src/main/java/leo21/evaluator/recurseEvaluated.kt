package leo21.evaluator

import leo14.lambda.value.Value
import leo21.prim.Prim
import leo21.type.Recurse

data class RecurseEvaluated(val value: Value<Prim>, val recurse: Recurse)