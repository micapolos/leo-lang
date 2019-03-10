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
		.termPush(leoWord fieldTo term(readWord fieldTo term(reflectFn(value))))
		?.termInvoke(value)

fun <V> Reader<V>.termPush(field: Field<Nothing>): Reader<V>? =
	termOrNull.orNullPush(field)?.let { copy(termOrNull = it) }

// TODO: This applyFn() should be incremental, to avoid quadratic cost.
fun <V> Reader<V>.termInvoke(value: V): Reader<V>? =
	if (termOrNull == null) this
	else termOrNull
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
							matchOrNull.bodyBinaryTrieMatch.value.apply(argument)?.let { applied ->
								copy(termOrNull = applied).termParse
							}
					}
				}
		}

val <V> Reader<V>.termParse: Reader<V>?
	get() =
		termOrNull?.structureTermOrNull?.fieldStream?.let { fieldStream ->
			copy(termOrNull = null).orNull
				.fold(fieldStream) { field ->
					if (this == null) null
					else if (termOrNull != null) termPush(field)
					else field.matchKey(leoWord) {
						matchFieldKey(readWord) {
							onlyFieldOrNull?.let { readField ->
								parseFn(readField).let { valueOrNull ->
									if (valueOrNull == null) termPush(leoWord fieldTo term(readWord fieldTo term(field)))
									else readPreprocessed(valueOrNull)
								}
							}
						}
					}
				}
		}

fun <V> Reader<V>.readPreprocessed(value: V): Reader<V>? =
	evaluator.evaluateFn(value)?.let { evaluator ->
		copy(evaluator = evaluator)
	}

// === bit stream

val <V> Reader<V>.bitStreamOrNull: Stream<EnumBit>?
	get() =
		evaluator.bitStreamOrNullFn()

// === reader instances

val emptyBitReader
	get() =
		Reader(
			Field<Nothing>::parseBit,
			EnumBit::reflect,
			emptyBitEvaluator.evaluator)

val emptyByteReader: Reader<Byte>
	get() =
		Reader(
			Field<Nothing>::parseByte,
			Byte::reflect,
			emptyByteEvaluator.evaluator)

val emptyCharacterReader: Reader<Character>
	get() =
		Reader(
			Field<Nothing>::parseCharacter,
			Character::reflect,
			emptyCharacterEvaluator.evaluator)

val emptyTokenReader: Reader<Token<Nothing>>
	get() =
		Reader(
			Field<Nothing>::parseToken,
			Token<Nothing>::reflect,
			emptyTokenEvaluator.evaluator)

val emptyFieldReader: Reader<Field<Nothing>>
	get() =
		Reader(
			Field<Nothing>::parseField,
			Field<Nothing>::reflect,
			emptyFieldEvaluator.evaluator)

val emptyTermReader: Reader<Term<Nothing>>
	get() =
		Reader(
			Field<Nothing>::parseTerm,
			Term<Nothing>::reflect,
			emptyTermReader.evaluator)
