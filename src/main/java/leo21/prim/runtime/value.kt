package leo21.prim.runtime

import leo14.lambda.Term
import leo14.lambda.value.Value
import leo14.lambda.value.value
import leo21.prim.Prim

val Term<Prim>.value: Value<Prim>
	get() =
		value(Prim::apply)
