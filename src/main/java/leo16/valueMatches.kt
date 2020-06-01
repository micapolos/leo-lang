package leo16

import leo.base.notNullIf
import leo.base.runIfNotNull
import leo13.Link
import leo16.names.*

fun Value.matches(value: Value): Boolean =
	force.forcedMatches(value)

fun Value.forcedMatches(value: Value): Boolean =
	null
		?: matchesAnythingOrNull
		?: matchesQuoteOrNull(value)
		?: matchesDefault(value)

val Value.matchesAnythingOrNull: Boolean?
	get() =
		match(_anything) { true }

fun Value.matchesQuoteOrNull(value: Value): Boolean? =
	matchPrefix(_quote) { rhs ->
		rhs == value
	}

fun Value.matchesDefault(value: Value): Boolean =
	runIfNotNull(linkOrNull) { it.matches(value) } ?: value.isEmpty

fun Link<Value, Field>.matches(value: Value): Boolean =
	null
		?: matchesAlternativeOrNull(value)
		?: matchesDefault(value)

fun Link<Value, Field>.matchesAlternativeOrNull(value: Value): Boolean? =
	head.matchPrefix(_or) { rhsPattern ->
		rhsPattern.matches(value) || tail.matches(value)
	}

fun Link<Value, Field>.matchesDefault(value: Value): Boolean =
	runIfNotNull(value.linkOrNull) { matches(it) } ?: false

fun Link<Value, Field>.matches(link: Link<Value, Field>): Boolean =
	head.matches(link.head) && tail.matches(link.tail)

fun Field.matches(field: Field): Boolean =
	null
		?: matchesMetaOrNull(field)
		?: matchesNativeOrNull(field)
		?: matchesFunctionOrNull(field)
		?: matchesDefault(field)

fun Field.matchesMetaOrNull(field: Field): Boolean? =
	matchPrefix(_meta) { rhsPattern ->
		runIfNotNull(rhsPattern.onlyFieldOrNull) { it.matches(field) }
	}

fun Field.matchesNativeOrNull(field: Field): Boolean? =
	notNullIf(this == _any(_native())) {
		field is NativeField
	}

fun Field.matchesFunctionOrNull(field: Field): Boolean? =
	matchPrefix(_function) { rhsPattern ->
		runIfNotNull(field.functionOrNull) { rhsPattern.matches(it) }
	}

fun Field.matchesDefault(field: Field): Boolean =
	when (this) {
		is SentenceField -> runIfNotNull(field.sentenceOrNull) { sentence.matches(it) }
		is FunctionField -> runIfNotNull(field.functionOrNull) { function == it }
		is NativeField -> runIfNotNull(field.theNativeOrNull) { native == it.value }
		is LazyField -> runIfNotNull(field.lazyOrNull) { lazy == it }
	} ?: false

fun Value.matches(function: Function): Boolean =
	function.patternValue == this

fun Sentence.matches(sentence: Sentence): Boolean =
	word == sentence.word && value.matches(sentence.value)

