package leo14.untyped.typed.lambda

val Typed.coreApply: Typed?
	get() =
		null
			?: applyGet

val Typed.applyGet: Typed?
	get() =
		matchPrefix { rhs ->
			rhs.get(this)
		}
