package leo15

val Typed.apply: Typed
	get() =
		null
			?: javaApply
			?: coreApply
			?: this