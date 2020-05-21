package leo16

import leo14.leonardoScript
import leo16.names.*

fun Value.apply(field: Field): Value? =
	null
		?: applyContent(field)
		?: applyGet(field)
		?: applyTake(field)
		?: applyThis(field)
//		?: applyQuote(field)
//		?: applyMeta(field)
		?: applyNothing(field)
		?: applyComment(field)
		?: applyScript(field)
		?: applyMatches(field)
		?: applyHash(field)
		?: applyEquals(field)
		?: applyAsText(field)
		?: applyLeonardo(field)

fun Value.applyGet(field: Field): Value? =
	matchEmpty {
		field.sentenceOrNull?.let { sentence ->
			sentence.value.getOrNull(sentence.word)
		}
	}

fun Value.applyContent(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(_content) { rhs ->
			rhs.contentOrNull
		}
	}

fun Value.applyTake(field: Field): Value? =
	matchInfix(_take, field) { lhs, rhs ->
		lhs.functionOrNull?.apply(rhs)
	}

fun Value.applyThis(field: Field): Value? =
	field.matchPrefix(_this) { rhs ->
		plus(rhs)
	}

fun Value.applyNothing(field: Field): Value? =
	matchEmpty {
		field.match(_nothing) { value() }
	}

fun Value.applyComment(field: Field): Value? =
	field.matchPrefix(_comment) { this }

fun Value.applyScript(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(_script) { rhs ->
			rhs.printed
		}
	}

fun Value.applyMatches(field: Field): Value? =
	field.matchPrefix(_matches) { rhs ->
		matches(rhs).field.value
	}

fun Value.applyQuote(field: Field): Value? =
	field.matchPrefix(_quote) { rhs ->
		plus(rhs)
	}

fun Value.applyMeta(field: Field): Value? =
	field.matchPrefix(_meta) { rhs ->
		plus(rhs)
	}

fun Value.applyEquals(field: Field): Value? =
	field.matchPrefix(_equals) { rhs ->
		this.equals(rhs).field.value
	}

fun Value.applyAsText(field: Field): Value? =
	field.matchPrefix(_as) { rhs ->
		rhs.match(_text) {
			printed.toString().field.value
		}
	}

fun Value.applyHash(field: Field): Value? =
	matchEmpty {
		field.matchPrefix(_hash) { rhs ->
			value(_hash(rhs.hashBigDecimal.field))
		}
	}

fun Value.applyLeonardo(field: Field): Value? =
	matchEmpty {
		field.match(_leonardo) {
			leonardoScript.asValue
		}
	}

