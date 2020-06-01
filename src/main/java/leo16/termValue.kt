package leo16

import leo.base.notNullIf
import leo13.Link
import leo15.lambda.Term
import leo15.lambda.unsafeUnchoice
import leo15.lambda.unsafeUnpair
import leo15.lambda.value
import leo16.names.*

fun Value.value(term: Term): Value =
	force.forcedValue(term)

fun Value.forcedValue(term: Term): Value =
	null
		?: anythingValueOrNull(term)
		?: quoteValueOrNull(term)
		?: defaultValue(term)

fun Value.anythingValueOrNull(term: Term): Value? =
	match(_anything) {
		term.value as Value
	}

fun Value.quoteValueOrNull(term: Term): Value? =
	matchPrefix(_quote) { rhs ->
		rhs.value(term)
	}

fun Value.defaultValue(term: Term): Value =
	linkOrNull?.value(term) ?: emptyValue

fun Link<Value, Field>.value(term: Term): Value =
	null
		?: alternativeValueOrNull(term)
		?: defaultValue(term)

fun Link<Value, Field>.alternativeValueOrNull(term: Term): Value? =
	head.matchPrefix(_or) { rhs ->
		term.unsafeUnchoice(2).let { (index, term) ->
			if (index == 0) rhs.value(term)
			else tail.value(term)
		}
	}

fun Link<Value, Field>.defaultValue(term: Term): Value =
	term.unsafeUnpair.let { termPair ->
		tail.value(termPair.first).plus(head.field(termPair.second))
	}

fun Field.field(term: Term): Field =
	null
		?: metaFieldOrNull(term)
		?: nativeFieldOrNull(term)
		?: functionFieldOrNull(term)
		?: defaultField(term)

fun Field.metaFieldOrNull(term: Term): Field? =
	matchPrefix(_meta) { rhs ->
		rhs.onlyFieldOrNull?.field(term)
	}

fun Field.nativeFieldOrNull(term: Term): Field? =
	notNullIf(this == _any(_native())) {
		term.value.nativeField
	}

fun Field.functionFieldOrNull(term: Term): Field? =
	matchPrefix(_function) { rhs ->
		this
	}

fun Field.defaultField(term: Term): Field =
	sentenceOrNull?.sentence(term)?.field ?: this

fun Sentence.sentence(term: Term): Sentence =
	word.sentenceTo(value.value(term))
