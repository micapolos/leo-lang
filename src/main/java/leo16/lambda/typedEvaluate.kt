package leo16.lambda

import leo15.lambda.eval

val Typed.evaluate: Typed
	get() =
		term.eval of type