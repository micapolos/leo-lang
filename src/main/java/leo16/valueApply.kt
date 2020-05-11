package leo16

import leo14.leonardoScript
import leo16.names.*

fun Value.apply(field: Field): Value? =
	null
		?: applyContent(field)
		?: applyGet(field)
		?: applyTake(field)
		?: applyThis(field)
		?: applyNothing(field)
		?: applyComment(field)
		?: applyScript(field)
		?: applyMatches(field)
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

fun Value.applyLeonardo(field: Field): Value? =
	matchEmpty {
		field.match(_leonardo) {
			leonardoScript.asValue
		}
	}

