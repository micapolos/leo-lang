package leo16

import leo.base.notNullIf
import leo.base.runIfNotNull
import leo13.Link
import leo13.Stack
import leo13.first
import leo13.push
import leo13.stack
import leo16.names.*

// TODO: Refactor, so pattern is this, and value is argument.

data class Matcher(val theSentenceStack: Stack<Sentence>)

val Stack<Sentence>.matcher get() = Matcher(this)
val emptyMatcher = stack<Sentence>().matcher
fun Matcher.push(sentence: Sentence) = theSentenceStack.push(sentence).matcher
fun Matcher.getOrNull(word: String) = theSentenceStack.first { it.word == word }

fun Value.matches(value: Value): Boolean =
	emptyMatcher.matches(this, value)

fun Matcher.matches(patternValue: Value, value: Value): Boolean =
	null
		?: matchesAnythingOrNull(patternValue)
		?: matchesTheOrNull(patternValue, value)
		?: matchesQuoteOrNull(patternValue, value)
		?: matchesDefault(patternValue, value)

fun matchesAnythingOrNull(patternValue: Value): Boolean? =
	patternValue.match(_anything) { true }

fun Matcher.matchesTheOrNull(patternValue: Value, value: Value): Boolean? =
	patternValue.matchPrefix(_the) { rhs ->
		rhs.matchWord { word ->
			getOrNull(word)?.let { patternSentence ->
				matches(patternSentence.field.value, value)
			}
		}
	}

fun matchesQuoteOrNull(patternValue: Value, value: Value): Boolean? =
	patternValue.matchPrefix(_quote) { rhs ->
		rhs == value
	}

fun Matcher.matchesDefault(patternValue: Value, value: Value): Boolean =
	runIfNotNull(patternValue.linkOrNull) { matches(it, value) } ?: value.isEmpty

fun Matcher.matches(patternLink: Link<Value, Field>, value: Value): Boolean =
	null
		?: matchesAlternativeOrNull(patternLink, value)
		?: matchesDefault(patternLink, value)

fun Matcher.matchesAlternativeOrNull(patternLink: Link<Value, Field>, value: Value): Boolean? =
	patternLink.head.matchPrefix(_or) { rhsPattern ->
		matches(rhsPattern, value) || matches(patternLink.tail, value)
	}

fun Matcher.matchesDefault(patternLink: Link<Value, Field>, value: Value): Boolean =
	runIfNotNull(value.linkOrNull) { matches(patternLink, it) } ?: false

fun Matcher.matches(patternLink: Link<Value, Field>, link: Link<Value, Field>): Boolean =
	matches(patternLink.head, link.head) && matches(patternLink.tail, link.tail)

fun Matcher.matches(patternField: Field, field: Field): Boolean =
	null
		?: matchesExactOrNull(patternField, field)
		?: matchesNativeOrNull(patternField, field)
		?: matchesFunctionOrNull2(patternField, field)
		?: matchesDefault(patternField, field)

fun Matcher.matchesExactOrNull(patternField: Field, field: Field): Boolean? =
	patternField.matchPrefix(_exact) { rhsPattern ->
		runIfNotNull(rhsPattern.onlyFieldOrNull) { matches(it, field) }
	}

fun matchesNativeOrNull(patternField: Field, field: Field): Boolean? =
	notNullIf(patternField == _any(_native())) {
		field is NativeField
	}

fun Matcher.matchesFunctionOrNull2(patternField: Field, field: Field): Boolean? =
	// TODO: Rename to _taking
	patternField.matchPrefix(_function) { rhsPattern ->
		runIfNotNull(field.functionOrNull) { matches(rhsPattern, it) }
	}

fun Matcher.matchesDefault(patternField: Field, field: Field): Boolean =
	when (patternField) {
		is SentenceField -> runIfNotNull(field.sentenceOrNull) { matches(patternField.sentence, it) }
		is FunctionField -> runIfNotNull(field.functionOrNull) { patternField.function == it }
		is NativeField -> runIfNotNull(field.theNativeOrNull) { patternField.native == it.value }
		is LazyField -> runIfNotNull(field.lazyOrNull) { patternField.lazy == it }
		is EvaluatedField -> TODO()
	} ?: false

fun matches(patternValue: Value, function: Function): Boolean =
	function.patternValue == patternValue

fun Matcher.matches(patternSentence: Sentence, sentence: Sentence): Boolean =
	patternSentence.word == sentence.word &&
		push(patternSentence).matches(patternSentence.value, sentence.value)

