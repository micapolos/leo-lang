package leo21.value

import leo14.lambda.Term
import leo14.lambda.value.Scope
import leo14.lambda.value.Value
import leo14.lambda.value.value
import leo21.prim.Prim
import leo21.prim.runtime.apply

fun Scope<Prim>.value(term: Term<Prim>): Value<Prim> =
	value(term, Prim::apply)