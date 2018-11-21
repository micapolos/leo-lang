package leo

import leo.base.*

data class Reader<V>(
	val parseFn: (Field<Nothing>) -> V?,
	val reflectFn: (V) -> Field<Nothing>,
	val evaluator: Evaluator<V>,
	val termOrNull: Term<Nothing>? = null)

fun <V> Reader<V>.read(value: V): Reader<V>? =
	if (!readerEnabled) readPreprocessed(value)
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
				.applyFn(argument)
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
	evaluator.evaluateFn(value)?.let { evaluator ->
		copy(evaluator = evaluator)
	}

// === bit stream

val <V> Reader<V>.bitStreamOrNull: Stream<Bit>?
	get() =
		evaluator.bitStreamOrNullFn()

fun <V> Reader<V>.apply(term: Term<Nothing>): Match? =
	evaluator.applyFn(term)

// === reader instances

val emptyTokenReader: Reader<Token<Nothing>>
	get() =
		Reader(
			Field<Nothing>::parseToken,
			Token<Nothing>::reflect,
			emptyTokenEvaluator.evaluator)

val emptyCharacterReader: Reader<Character>
	get() =
		Reader(
			Field<Nothing>::parseCharacter,
			Character::reflect,
			emptyCharacterEvaluator.evaluator)

val emptyByteReader: Reader<Byte>
	get() =
		Reader(
			Field<Nothing>::parseByte,
			Byte::reflect,
			emptyByteEvaluator.evaluator)

val emptyBitReader
	get() =
		Reader(
			Field<Nothing>::parseBit,
			Bit::reflect,
			emptyBitEvaluator.evaluator)

