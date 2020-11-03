package leo21.evaluated

import leo14.Script
import leo14.lambda.value.Value
import leo14.lambda.value.term
import leo14.lambda.value.value
import leo21.prim.Prim
import leo21.prim.prim
import leo21.type.Type
import leo21.type.type
import leo21.typed.Typed
import leo21.typed.script

data class Evaluated(val value: Value<Prim>, val type: Type)

val emptyEvaluated = Evaluated(value(prim(0)), type())

val Evaluated.typed: Typed
	get() =
		Typed(value.term, type)

val Evaluated.script: Script
	get() =
		Typed(value.term, type).script