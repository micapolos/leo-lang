package leo21.evaluated

import leo14.lambda.value.Value
import leo21.prim.Prim
import java.lang.reflect.Type

data class Evaluated(val value: Value<Prim>, val type: Type)