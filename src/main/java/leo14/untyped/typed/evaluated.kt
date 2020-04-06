package leo14.untyped.typed

import leo14.Script
import leo14.lambda.runtime.Value

data class Evaluated(val typeScript: Script, val value: Value)

fun Script.evaluated(value: Value) = Evaluated(this, value)

