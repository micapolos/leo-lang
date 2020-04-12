package leo14.untyped.typed.lambda

val Typed.coreApply: Typed?
	get() =
		null
			?: applyGet
			?: applyMake

val Typed.applyGet: Typed?
	get() =
		matchLink { rhs ->
			rhs.matchName { name ->
				get(name)
			}
		}

val Typed.applyMake: Typed?
	get() =
		matchLink { rhs ->
			rhs.matchName { name ->
				make(name)
			}
		}
