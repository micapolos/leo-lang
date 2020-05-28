package leo16

import leo.base.runIfNotNull
import leo13.Link
import leo16.names.*

fun Value.matches(value: Value): Boolean =
	value.isAnything || matchesDefault(value)

val Value.isAnything: Boolean
	get() =
		// TODO: Remove any.
		this == value(_any()) || this == value(_anything())

fun Value.matchesDefault(value: Value): Boolean =
	runIfNotNull(value.linkOrNull) { matches(it) } ?: isEmpty

fun Value.matches(link: Link<Value, Field>): Boolean =
	null
		?: matchesAlternativeOrNull(link)
		?: matchesExact(link)

fun Value.matchesAlternativeOrNull(link: Link<Value, Field>): Boolean? =
	link.head.matchPrefix(_or) { rhs ->
		matches(rhs) || matches(link.tail)
	}

fun Value.matchesExact(link: Link<Value, Field>): Boolean =
	linkOrNull?.matches(link) ?: false

fun Link<Value, Field>.matches(link: Link<Value, Field>): Boolean =
	head.matches(link.head) && tail.matches(link.tail)

fun Field.matches(field: Field): Boolean =
	null
		?: matchesExactOrNull(field)
		?: matchesNativeOrNull(field)
		?: matchesFunctionOrNull2(field)
		?: matchesDefault(field)

fun Field.matchesExactOrNull(field: Field): Boolean? =
	field.matchPrefix(_exact) { rhs ->
		runIfNotNull(rhs.onlyFieldOrNull) { matches(it) }
	}

fun Field.matchesNativeOrNull(field: Field): Boolean? =
	field.match(_native) {
		this is NativeField
	}

fun Field.matchesFunctionOrNull2(field: Field): Boolean? =
	// TODO: Rename to _taking
	field.matchPrefix(_function) { rhs ->
		functionOrNull?.matches(rhs)
	}

fun Field.matchesDefault(field: Field): Boolean =
	when (field) {
		is SentenceField -> sentenceOrNull?.matches(field.sentence)
		is FunctionField -> functionOrNull?.run { this == field.function }
		is NativeField -> theNativeOrNull?.run { value == field.native }
		is LazyField -> lazyOrNull?.run { this == field.lazy }
		is EvaluatedField -> TODO()
	} ?: false

fun Function.matches(value: Value): Boolean =
	patternValue == value

fun Sentence.matches(sentence: Sentence): Boolean =
	word == sentence.word && value.matches(sentence.value)

