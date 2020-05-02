package leo16

import leo14.*
import leo14.Number
import leo15.*

fun Value.apply(field: Field): Value? =
	null
		?: applyThing(field)
		?: applyGet(field)
		?: applyGive(field)
		?: applyThis(field)
		?: applyNothing(field)
		?: applyTextPlusText(field)
		?: applyTextLength(field)
		?: applyNumberPlusNumber(field)
		?: applyNumberMinusNumber(field)
		?: applyNumberTimesNumber(field)

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

fun Value.applyThis(field: Field): Value? =
	field.matchPrefix(thisName) { rhs ->
		plus(rhs)
	}

fun Value.applyNothing(field: Field): Value? =
	matchEmpty {
		field.match(nothingName) { value() }
	}

fun Value.applyTextPlusText(field: Field): Value? =
	matchText { lhsString ->
		field.matchPrefix(plusName) { rhs ->
			rhs.matchText { rhsString ->
				lhsString.plus(rhsString).literal.field.value
			}
		}
	}

fun Value.applyTextLength(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(lengthName) { rhs ->
			rhs.matchText { text ->
				literal(text.length).field.value
			}
		}
	}

fun Value.applyNumberPlusNumber(field: Field): Value? =
	applyNumberOpNumber(field, plusName, Number::plus)

fun Value.applyNumberMinusNumber(field: Field): Value? =
	applyNumberOpNumber(field, minusName, Number::minus)

fun Value.applyNumberTimesNumber(field: Field): Value? =
	applyNumberOpNumber(field, timesName, Number::times)

fun Value.applyNumberOpNumber(field: Field, word: String, fn: Number.(Number) -> Number): Value? =
	matchNumber { lhsNumber ->
		field.matchPrefix(word) { rhs ->
			rhs.matchNumber { rhsNumber ->
				literal(lhsNumber.fn(rhsNumber)).field.value
			}
		}
	}