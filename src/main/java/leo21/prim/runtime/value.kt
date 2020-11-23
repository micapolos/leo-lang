package leo21.prim.runtime

import leo14.lambda.Term
import leo14.lambda.value.Value
import leo14.lambda.value.eitherFirst
import leo14.lambda.value.eitherSecond
import leo14.lambda.value.value
import leo21.evaluated.nilValue
import leo21.prim.Prim

val Term<Prim>.value: Value<Prim>
	get() =
		value(Prim::apply)

val Boolean.value: Value<Prim>
	get() =
		if (this) nilValue.eitherSecond.eitherFirst
		else nilValue.eitherSecond