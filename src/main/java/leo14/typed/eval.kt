package leo14.typed

import leo14.lambda.eval

val Typed<Any>.anyEval: Typed<Any>
	get() =
		term.eval of type
