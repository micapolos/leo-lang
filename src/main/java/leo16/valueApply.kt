package leo16

import leo13.onlyOrNull
import leo15.appendName
import leo15.giveName
import leo15.thingName
import leo15.thisName

fun Value.apply(field: Field): Value? =
	null
		?: applyAppend(field)
		?: applyThing(field)
		?: applyGet(field)
		?: applyGive(field)
		?: applyThis(field)

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

fun Value.applyThis(field: Field): Value? =
	field.matchPrefix(thisName) { rhs ->
		plus(rhs)
	}
