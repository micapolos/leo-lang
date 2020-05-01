package leo16

import leo15.giveName
import leo15.thingName

fun Value.apply(line: Line): Value? =
	null
		?: applyThing(line)
		?: applyGet(line)
		?: applyGive(line)

fun Value.applyGet(line: Line): Value? =
	line.matchWord { word ->
		getOrNull(word)
	}

fun Value.applyThing(line: Line): Value? =
	line.match(thingName) {
		thingOrNull
	}

fun Value.applyGive(line: Line): Value? =
	line.matchPrefix(giveName) { rhs ->
		functionOrNull?.invoke(rhs)
	}
