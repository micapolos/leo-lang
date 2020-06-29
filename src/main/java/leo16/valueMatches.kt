package leo16

import leo.base.runIfNotNull
import leo16.names.*

fun Value.matches(value: Value): Boolean =
	force.forcedMatches(value)

fun Value.forcedMatches(value: Value): Boolean =
	null
		?: matchesAnythingOrNull
		?: matchesQuoteOrNull(value)
		?: matchesNativeOrNull(value)
		?: matchesFunctionOrNull(value)
		?: matchesDefault(value)

val Value.matchesAnythingOrNull: Boolean?
	get() =
		match(_anything) {
			true
		}

fun Value.matchesQuoteOrNull(value: Value): Boolean? =
	matchPrefix(_quote) { rhs ->
		rhs == value
	}

fun Value.matchesNativeOrNull(value: Value): Boolean? =
	matchPrefix(_any) { rhs ->
		rhs.match(_native) {
			value is NativeValue
		}
	}

fun Value.matchesFunctionOrNull(value: Value): Boolean? =
	matchPrefix(_function) { rhs ->
		value.functionOrNull?.let { function ->
			function.patternValue == rhs
		}
	}

fun Value.matchesDefault(value: Value): Boolean =
	when (this) {
		EmptyValue -> this == value
		is LinkValue -> link.matches(value)
		is NativeValue -> this == value
		is FunctionValue -> this == value
		is LazyValue -> this == value
		is FuncValue -> TODO()
	}

fun ValueLink.matches(value: Value): Boolean =
	null
		?: matchesAlternativeOrNull(value)
		?: matchesDefault(value)

fun ValueLink.matchesAlternativeOrNull(value: Value): Boolean? =
	lastSentence.matchPrefix(_or) { rhs ->
		rhs.matches(value) || previousValue.matches(value)
	}

fun ValueLink.matchesDefault(value: Value): Boolean =
	runIfNotNull(value.linkOrNull) { matches(it) } ?: false

fun ValueLink.matches(valueLink: ValueLink): Boolean =
	lastSentence.matches(valueLink.lastSentence) &&
		previousValue.matches(valueLink.previousValue)

fun Sentence.matches(sentence: Sentence): Boolean =
	null
		?: matchesMetaOrNull(sentence)
		?: matchesDefault(sentence)

fun Sentence.matchesMetaOrNull(sentence: Sentence): Boolean? =
	matchPrefix(_meta) { rhs ->
		rhs.onlySentenceOrNull?.matchesDefault(sentence)
	}

fun Sentence.matchesDefault(sentence: Sentence): Boolean =
	word == sentence.word && rhsValue.matches(sentence.rhsValue)
