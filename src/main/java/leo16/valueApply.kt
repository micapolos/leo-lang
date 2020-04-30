package leo16

val Value.apply: Value?
	get() =
		null
			?: applyGet

val Value.applyGet: Value?
	get() =
		matchLink { lhs, word, rhs ->
			lhs.matchEmpty {
				rhs.getOrNull(word)
			}
		}
