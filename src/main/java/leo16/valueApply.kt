package leo16

import leo14.leonardoScript
import leo16.names.*

fun Value.apply(sentence: Sentence): Value? =
	null
		?: applyThing(sentence)
		?: applyImplicitGet(sentence)
		?: applyGet(sentence)
		?: applyMake(sentence)
		?: applyForce(sentence)
		?: applyTake(sentence)
		?: applyThis(sentence)
		?: applyQuote(sentence)
		?: applyMeta(sentence)
		?: applyNothing(sentence)
		?: applyComment(sentence)
		?: applyMatches(sentence)
		?: applyHash(sentence)
		?: applyEquals(sentence)
		?: applyAsText(sentence)
		?: applyMatching(sentence)
		?: applyLeonardo(sentence)

fun Value.applyImplicitGet(sentence: Sentence): Value? =
	matchEmpty {
		sentence.rhsValue.getOrNull(sentence.word)
	}

fun Value.applyGet(sentence: Sentence): Value? =
	sentence.matchPrefix(_get) { rhs ->
		rhs.matchWord { word ->
			getOrNull(word)
		}
	}

fun Value.applyMake(sentence: Sentence): Value? =
	sentence.matchPrefix(_make) { rhs ->
		rhs.matchWord { word ->
			make(word)
		}
	}

fun Value.applyThing(sentence: Sentence): Value? =
	matchEmpty {
		sentence.matchPrefix(_thing) { rhs ->
			rhs.thingOrNull
		}
	}

fun Value.applyTake(sentence: Sentence): Value? =
	matchInfix(_take, sentence) { lhs, rhs ->
		lhs.functionOrNull?.apply(rhs)
	}

fun Value.applyThis(sentence: Sentence): Value? =
	sentence.matchPrefix(_this) { rhs ->
		rhs.onlySentenceOrNull?.let { sentence ->
			plus(sentence)
		}
	}

fun Value.applyNothing(sentence: Sentence): Value? =
	matchEmpty {
		sentence.match(_nothing) { value() }
	}

fun Value.applyComment(sentence: Sentence): Value? =
	sentence.matchPrefix(_comment) { this }

fun Value.applyMatches(sentence: Sentence): Value? =
	sentence.matchPrefix(_matches) { rhs ->
		matches(rhs).sentence.onlyValue
	}

fun Value.applyForce(sentence: Sentence): Value? =
	matchEmpty {
		sentence.matchPrefix(_force) { rhs ->
			rhs.forceOrNull
		}
	}

fun Value.applyQuote(sentence: Sentence): Value? =
	matchEmpty {
		sentence.matchPrefix(_quote) { rhs ->
			rhs
		}
	}

fun Value.applyMeta(sentence: Sentence): Value? =
	sentence.matchPrefix(_meta) { rhs ->
		rhs.onlySentenceOrNull?.let { sentence ->
			plus(sentence)
		}
	}

fun Value.applyEquals(sentence: Sentence): Value? =
	sentence.matchPrefix(_equals) { rhs ->
		(this == rhs).sentence.onlyValue
	}

fun Value.applyAsText(sentence: Sentence): Value? =
	sentence.matchPrefix(_as) { rhs ->
		rhs.match(_text) {
			printed.toString().value
		}
	}

fun Value.applyMatching(sentence: Sentence): Value? =
	sentence.matchPrefix(_matching) { rhs ->
		matching(rhs)
	}

fun Value.applyHash(sentence: Sentence): Value? =
	matchEmpty {
		sentence.matchPrefix(_hash) { rhs ->
			value(_hash(rhs.hashBigDecimal.sentence))
		}
	}

fun Value.applyLeonardo(sentence: Sentence): Value? =
	matchEmpty {
		sentence.match(_leonardo) {
			leonardoScript.asValue
		}
	}

