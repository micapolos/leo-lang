package leo

import leo.base.*

data class Reader<V>(
	val parseFn: (Field<Nothing>) -> V?,
	val reflectFn: (V) -> Field<Nothing>,
	val evaluator: Evaluator<V>,
	val termOrNull: Term<Nothing>?)

fun <V> Reader<V>.read(value: V): Reader<V>? =
	if (!readersEnabled || !byteReaderEnabled) readPreprocessed(value)
	else this
		.termPush(reflectFn(value))
		.termInvoke(value)

fun <V> Reader<V>.termPush(field: Field<Nothing>): Reader<V> =
	copy(termOrNull = termOrNull.push(field))

fun <V> Reader<V>.termInvoke(value: V): Reader<V>? =
	if (termOrNull == null) this
	else termOrNull
		.push(leoWord fieldTo readWord.term)
		.let { argument ->
			evaluator
				.apply(argument)
				.let { matchOrNull ->
					if (matchOrNull == null)
						copy(termOrNull = null).readPreprocessed(value)
					else when (matchOrNull.bodyBinaryTrieMatch) {
						is BinaryTrie.Match.Partial ->
							this // partial - continue
						is BinaryTrie.Match.Full ->
							copy(termOrNull = matchOrNull.bodyBinaryTrieMatch.value.apply(argument)).termParse
					}
				}
		}

val <V> Reader<V>.termParse: Reader<V>?
	get() =
		copy(termOrNull = null).orNull
			.fold(termOrNull?.fieldStreamOrNull?.reverse) { field ->
				if (this == null) null
				else if (termOrNull != null) termPush(field)
				else parseFn(field).let { valueOrNull ->
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
		evaluator.bitStreamOrNull()

fun <V> Reader<V>.apply(term: Term<Nothing>): Match? =
	evaluator.apply(term)