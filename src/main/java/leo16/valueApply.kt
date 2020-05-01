package leo16

import leo13.onlyOrNull
import leo15.*

fun Value.apply(line: Line): Value? =
	null
		?: applyLast(line)
		?: applyPrevious(line)
		?: applyAppend(line)
		?: applyThing(line)
		?: applyGet(line)
		?: applyGive(line)

fun Value.applyLast(line: Line): Value? =
	matchEmpty {
		line.matchPrefix(lastName) { rhs ->
			rhs.lastOrNull
		}
	}

fun Value.applyPrevious(line: Line): Value? =
	matchEmpty {
		line.matchPrefix(previousName) { rhs ->
			rhs.previousOrNull
		}
	}

fun Value.applyAppend(line: Line): Value? =
	line.matchPrefix(appendName) { rhs ->
		rhs.structOrNull?.lineStack?.onlyOrNull?.let { line ->
			listAppendOrNull(line)
		}
	}

fun Value.applyGet(line: Line): Value? =
	matchEmpty {
		line.value.getOrNull(line.word)
	}

fun Value.applyThing(line: Line): Value? =
	matchEmpty {
		line.matchPrefix(thingName) { rhs ->
			rhs.thingOrNull
		}
	}

fun Value.applyGive(line: Line): Value? =
	line.matchPrefix(giveName) { rhs ->
		functionOrNull?.invoke(rhs)
	}
