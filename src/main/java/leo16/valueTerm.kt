package leo16

import leo.base.ifOrNull
import leo.base.notNullIf
import leo15.lambda.Term
import leo15.lambda.choiceTerm
import leo15.lambda.idTerm
import leo15.lambda.valueTerm
import leo15.plus
import leo16.names.*

fun Value.termOrNull(value: Value): Term? =
	force.forcedTermOrNull(value)

fun Value.forcedTermOrNull(value: Value): Term? =
	null
		?: anythingTermOrNull(value)
		?: quoteTermOrNull(value)
		?: nativeTermOrNull(value)
		?: functionTermOrNull(value)
		?: defaultTermOrNull(value)

fun Value.anythingTermOrNull(value: Value): Term? =
	match(_anything) {
		value.valueTerm
	}

fun Value.quoteTermOrNull(value: Value): Term? =
	matchPrefix(_quote) { rhs ->
		rhs.defaultTermOrNull(value)
	}

fun Value.nativeTermOrNull(value: Value): Term? =
	matchPrefix(_any) { rhs ->
		rhs.match(_native) {
			value.theNativeOrNull?.let { it.value.valueTerm }
		}
	}

fun Value.functionTermOrNull(value: Value): Term? =
	matchPrefix(_function) { rhs ->
		value.matchFunction(rhs) { compiled ->
			compiled.valueTerm // TODO: Replace with compiled function
		}
	}

fun Value.defaultTermOrNull(value: Value): Term? =
	when (this) {
		EmptyValue -> notNullIf(this == value) { idTerm }
		is LinkValue -> link.termOrNull(value)
		is NativeValue -> notNullIf(this == value) { idTerm }
		is FunctionValue -> notNullIf(this == value) { idTerm }
		is LazyValue -> notNullIf(this == value) { idTerm }
	}

fun ValueLink.termOrNull(value: Value): Term? =
	null
		?: alternativeTermOrNull(value)
		?: defaultTermOrNull(value)

fun ValueLink.alternativeTermOrNull(value: Value): Term? =
	lastSentence.matchPrefix(_or) { rhs ->
		null
			?: rhs.termOrNull(value)?.let { choiceTerm(2, 0, it) }
			?: previousValue.termOrNull(value)?.let { choiceTerm(2, 1, it) }
	}

fun ValueLink.defaultTermOrNull(value: Value): Term? =
	value.linkOrNull?.let { valueLink ->
		lastSentence.termOrNull(valueLink.lastSentence)?.let { lastSentenceTerm ->
			previousValue.termOrNull(valueLink.previousValue)?.let { previousValueTerm ->
				previousValueTerm.plus(lastSentenceTerm)
			}
		}
	}

fun Sentence.termOrNull(field: Sentence): Term? =
	null
		?: metaTermOrNull(field)
		?: defaultTermOrNull(field)

fun Sentence.metaTermOrNull(field: Sentence): Term? =
	matchPrefix(_meta) { rhs ->
		rhs.onlySentenceOrNull?.defaultTermOrNull(field)
	}

fun Sentence.defaultTermOrNull(sentence: Sentence): Term? =
	ifOrNull(word == sentence.word) {
		rhsValue.termOrNull(sentence.rhsValue)
	}
