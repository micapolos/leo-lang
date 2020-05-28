package leo16

import leo.base.runIfNotNull
import leo13.Link
import leo16.names.*

fun Value.matches2(value: Value): Boolean =
	value.isAnything || matchesDefault2(value)

val Value.isAnything: Boolean
	get() =
		// TODO: Remove any.
		this == value(_any()) || this == value(_anything())

fun Value.matchesDefault2(value: Value): Boolean =
	runIfNotNull(value.linkOrNull) { matches2(it) } ?: isEmpty

fun Value.matches2(link: Link<Value, Field>): Boolean =
	null
		?: matchesAlternativeOrNull2(link)
		?: matchesExact2(link)

fun Value.matchesAlternativeOrNull2(link: Link<Value, Field>): Boolean? =
	link.head.matchPrefix(_or) { rhs ->
		matches2(rhs) || matches2(link.tail)
	}

fun Value.matchesExact2(link: Link<Value, Field>): Boolean =
	linkOrNull?.matches2(link) ?: false

fun Link<Value, Field>.matches2(link: Link<Value, Field>): Boolean =
	head.matches2(link.head) && tail.matches2(link.tail)

fun Field.matches2(field: Field): Boolean =
	null
		?: matchesExactOrNull2(field)
		?: matchesNativeOrNull2(field)
		?: matchesTakingOrNull2(field)
		?: matchesDefault2(field)

fun Field.matchesExactOrNull2(field: Field): Boolean? =
	field.matchPrefix(_exact) { rhs ->
		runIfNotNull(rhs.onlyFieldOrNull) { matches(it) }
	}

fun Field.matchesNativeOrNull2(field: Field): Boolean? =
	field.match(_native) {
		this is NativeField
	}

fun Field.matchesTakingOrNull2(field: Field): Boolean? =
	field.matchPrefix(_taking) { rhs ->
		functionOrNull?.matches2(rhs)
	}

fun Field.matchesDefault2(field: Field): Boolean =
	when (field) {
		is SentenceField -> sentenceOrNull?.matches2(field.sentence)
		is FunctionField -> functionOrNull?.run { this == field.function }
		is NativeField -> theNativeOrNull?.run { value == field.native }
		is ChoiceField -> choiceOrNull?.run { this == field.choice }
		is LazyField -> lazyOrNull?.run { this == field.lazy }
		is EvaluatedField -> TODO()
	} ?: false

fun Function.matches2(value: Value): Boolean =
	pattern.asValue == value

fun Sentence.matches2(sentence: Sentence): Boolean =
	word == sentence.word && value.matches2(sentence.value)

