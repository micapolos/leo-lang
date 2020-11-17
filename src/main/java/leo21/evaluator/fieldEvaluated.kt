package leo21.evaluator

import leo14.lambda.value.Value
import leo21.prim.Prim
import leo21.type.Field

data class FieldEvaluated(val value: Value<Prim>, val field: Field)

val FieldEvaluated.rhs: Evaluated
	get() =
		value of field.rhs

val FieldEvaluated.name: String
	get() =
		field.name