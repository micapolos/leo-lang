package leo21.evaluator

import leo14.lambda.value.Value
import leo21.prim.Prim
import leo21.type.Recursive

data class RecursiveEvaluated(val value: Value<Prim>, val recursive: Recursive)