package leo14.untyped.typed.lambda

val Typed.apply: Typed
	get() =
		null
			?: javaApply
			?: coreApply
			?: this