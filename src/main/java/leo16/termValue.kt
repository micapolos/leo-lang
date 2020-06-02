package leo16

import leo.base.notNullIf
import leo15.lambda.Term
import leo15.lambda.unsafeUnchoice
import leo15.lambda.unsafeUnpair
import leo15.lambda.value
import leo16.names.*

// TODO: Perform static optimization

fun Value.value(term: Term): Value =
	force.forcedValue(term)

fun Value.forcedValue(term: Term): Value =
	null
		?: anythingValueOrNull(term)
		?: quoteValueOrNull(term)
		?: nativeValueOrNull(term)
		?: functionValueOrNull(term)
		?: defaultValue(term)

fun Value.anythingValueOrNull(term: Term): Value? =
	match(_anything) {
		term.value as Value
	}

fun Value.quoteValueOrNull(term: Term): Value? =
	matchPrefix(_quote) { rhs ->
		rhs.defaultValue(term)
	}

fun Value.nativeValueOrNull(term: Term): Value? =
	notNullIf(this == leo16.value(_any(_native()))) {
		term.value.nativeValue
	}

fun Value.functionValueOrNull(term: Term): Value? =
	matchPrefix(_function) { rhs ->
		this
	}

fun Value.defaultValue(term: Term): Value =
	linkOrNull?.value(term) ?: emptyValue

fun ValueLink.value(term: Term): Value =
	null
		?: alternativeValueOrNull(term)
		?: defaultValue(term)

fun ValueLink.alternativeValueOrNull(term: Term): Value? =
	lastSentence.matchPrefix(_or) { rhs ->
		term.unsafeUnchoice(2).let { (index, term) ->
			if (index == 0) rhs.value(term)
			else previousValue.value(term)
		}
	}

fun ValueLink.defaultValue(term: Term): Value =
	if (previousValue.isEmpty) leo16.value(lastSentence.field(term))
	else term.unsafeUnpair.let { termPair ->
		previousValue.value(termPair.first).plus(lastSentence.field(termPair.second))
	}

fun Sentence.field(term: Term): Sentence =
	null
		?: metaSentenceOrNull(term)
		?: defaultSentence(term)

fun Sentence.metaSentenceOrNull(term: Term): Sentence? =
	matchPrefix(_meta) { rhs ->
		rhs.onlySentenceOrNull?.defaultSentence(term)
	}

fun Sentence.defaultSentence(term: Term): Sentence =
	word.sentenceTo(rhsValue.value(term))
