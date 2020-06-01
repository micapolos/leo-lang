package leo16

import leo.base.ifOrNull
import leo.base.runIfNotNull
import leo13.Link
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
		?: defaultTermOrNull(value)

fun Value.anythingTermOrNull(value: Value): Term? =
	match(_anything) {
		value.valueTerm
	}

fun Value.quoteTermOrNull(value: Value): Term? =
	matchPrefix(_quote) { rhs ->
		rhs.termOrNull(value)
	}

fun Value.defaultTermOrNull(value: Value): Term? =
	linkOrNull?.term(value) ?: idTerm

fun Link<Value, Field>.term(value: Value): Term? =
	null
		?: alternativeTermOrNull(value)
		?: defaultTermOrNull(value)

fun Link<Value, Field>.alternativeTermOrNull(value: Value): Term? =
	head.matchPrefix(_or) { rhs ->
		null
			?: rhs.termOrNull(value)?.let { choiceTerm(2, 0, it) }
			?: tail.termOrNull(value)?.let { choiceTerm(2, 1, it) }
	}

fun Link<Value, Field>.defaultTermOrNull(value: Value): Term? =
	value.linkOrNull?.let { valueLink ->
		head.termOrNull(valueLink.head)?.let { headTerm ->
			tail.termOrNull(valueLink.tail)?.let { tailTerm ->
				tailTerm.plus(headTerm)
			}
		}
	}

fun Field.termOrNull(field: Field): Term? =
	null
		?: metaTermOrNull(field)
		?: nativeTermOrNull(field)
		?: functionTermOrNull(field)
		?: defaultTermOrNull(field)

fun Field.metaTermOrNull(field: Field): Term? =
	matchPrefix(_meta) { rhs ->
		rhs.onlyFieldOrNull?.termOrNull(field)
	}

fun Field.nativeTermOrNull(field: Field): Term? =
	ifOrNull(this == _any(_native())) {
		field.theNativeOrNull?.run { value.valueTerm }
	}

fun Field.functionTermOrNull(field: Field): Term? =
	matchPrefix(_function) { rhs ->
		TODO()
	}

fun Field.defaultTermOrNull(field: Field): Term? =
	null
		?: sentenceOrNull?.runIfNotNull(field.sentenceOrNull) { termOrNull(it) }
		?: ifOrNull(this == field) { idTerm }

fun Sentence.termOrNull(sentence: Sentence): Term? =
	ifOrNull(word == sentence.word) {
		value.termOrNull(sentence.value)
	}
