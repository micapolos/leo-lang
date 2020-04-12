package leo14.untyped.typed.lambda

val Compiled.apply: Compiled
	get() =
		null
			?: nativeApply
			?: coreApply
			?: this