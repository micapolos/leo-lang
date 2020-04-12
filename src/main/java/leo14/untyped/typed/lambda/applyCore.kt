package leo14.untyped.typed.lambda

val Compiled.coreApply: Compiled?
	get() =
		null
			?: applyGet
			?: applyMake

val Compiled.applyGet: Compiled?
	get() =
		matchLink { rhs ->
			rhs.matchName { name ->
				get(name)
			}
		}

val Compiled.applyMake: Compiled?
	get() =
		matchLink { rhs ->
			rhs.matchName { name ->
				make(name)
			}
		}
