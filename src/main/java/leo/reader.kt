package leo

import leo.base.*

data class Reader<V>(
	val parseFn: (Field<Nothing>) -> V?,
	val reflectFn: (V) -> Field<Nothing>,
	val applyFn: (Term<Nothing>) -> Term<Nothing>?,
	val bitStreamOrNullFn: () -> Stream<Bit>?,
	val evaluator: Evaluator<V>,
	val termOrNull: Term<Nothing>?)

fun <V> Reader<V>.read(value: V): Reader<V>? =
	this
		.termPush(leoReadField(value))
		.termInvoke
		.termParse

fun <V> Reader<V>.termPush(field: Field<Nothing>): Reader<V> =
	copy(termOrNull = termOrNull.push(field))

val <V> Reader<V>.termInvoke: Reader<V>
	get() =
		if (termOrNull == null) this
		else copy(termOrNull = apply(termOrNull) ?: termOrNull)

val <V> Reader<V>.termParse: Reader<V>?
	get() =
		copy(termOrNull = null).orNull
			.fold(termOrNull?.fieldStreamOrNull?.reverse) { field ->
				if (this == null) null
				else if (termOrNull != null) termPush(field)
				else if (field == leoWord fieldTo continueWord.term) this
				else parseLeoRead(field).let { valueOrNull ->
					if (valueOrNull == null) termPush(field)
					else readPreprocessed(valueOrNull)
				}
			}

fun <V> Reader<V>.readPreprocessed(value: V): Reader<V>? =
	evaluator.evaluate(value)?.let { evaluator ->
		copy(evaluator = evaluator)
	}

// === leo read bit

fun <V> Reader<V>.leoReadField(value: V): Field<Nothing> =
	leoWord fieldTo term(readWord fieldTo reflectFn(value).term)

fun <V> Reader<V>.parseLeoRead(term: Field<Nothing>): V? =
	term.get(leoWord)?.let { theLeoTerm ->
		theLeoTerm.value?.match(readWord) { readTerm ->
			readTerm?.onlyFieldOrNull?.let { field ->
				parseFn(field)
			}
		}
	}

// === bit stream

val <V> Reader<V>.bitStreamOrNull: Stream<Bit>?
	get() =
		bitStreamOrNullFn()

fun <V> Reader<V>.apply(term: Term<Nothing>): Term<Nothing>? =
	applyFn(term)