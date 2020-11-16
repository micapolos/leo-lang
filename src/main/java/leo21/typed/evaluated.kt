package leo21.typed

import leo14.lambda.value.Value
import leo14.lambda.value.plus
import leo21.prim.Prim
import leo21.type.Line
import leo21.type.Type

fun Typed<Value<Prim>, Type>.plus(rhs: Typed<Value<Prim>, Line>): Typed<Value<Prim>, Type> =
	plus(rhs) { plus(it) }
