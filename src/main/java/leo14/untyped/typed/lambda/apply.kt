package leo14.untyped.typed.lambda

val Typed.apply: Typed
	get() =
		null
			?: nativeApply
			?: coreApply
			?: this