package leo16

import leo15.thingName

val Value.apply: Value?
	get() =
		null
			?: applyThing
			?: applyGet

val Value.applyGet: Value?
	get() =
		matchLink { lhs, word, rhs ->
			lhs.matchEmpty {
				rhs.getOrNull(word)
			}
		}

val Value.applyThing: Value?
	get() =
		matchPrefix(thingName) { rhs ->
			rhs.thingOrNull
		}
