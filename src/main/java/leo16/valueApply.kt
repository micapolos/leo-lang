package leo16

import leo13.onlyOrNull
import leo15.*

fun Value.apply(field: Field): Value? =
	null
		?: applyLast(field)
		?: applyPrevious(field)
		?: applyAppend(field)
		?: applyThing(field)
		?: applyGet(field)
		?: applyGive(field)

fun Value.applyLast(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(lastName) { rhs ->
			rhs.lastOrNull
		}
	}

fun Value.applyPrevious(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(previousName) { rhs ->
			rhs.previousOrNull
		}
	}

fun Value.applyAppend(field: Field): Value? =
	field.matchPrefix(appendName) { rhs ->
		rhs.fieldStack.onlyOrNull?.let { field ->
			listAppendOrNull(field)
		}
	}

fun Value.applyGet(field: Field): Value? =
	matchEmpty {
		field.sentenceOrNull?.let { sentence ->
			sentence.value.getOrNull(sentence.word)
		}
	}

fun Value.applyThing(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(thingName) { rhs ->
			rhs.thingOrNull
		}
	}

fun Value.applyGive(field: Field): Value? =
	field.matchPrefix(giveName) { rhs ->
		functionOrNull?.invoke(rhs)
	}
