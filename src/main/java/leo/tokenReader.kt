package leo

import leo.base.*

data class TokenReader(
	val tokenEvaluator: TokenEvaluator,
	val termOrNull: Term<Nothing>?)

val emptyTokenReader =
	TokenReader(emptyTokenEvaluator, null)

fun TokenReader.read(token: Token<Nothing>): TokenReader? =
	if (!readersEnabled || !byteReaderEnabled) readPreprocessed(token)
	else this
		.termPush(token.reflect)
		.termInvoke(token)

fun TokenReader.termPush(field: Field<Nothing>): TokenReader =
	copy(termOrNull = termOrNull.push(field))

fun TokenReader.termInvoke(token: Token<Nothing>): TokenReader? =
	if (termOrNull == null) this
	else termOrNull
		.push(leoWord fieldTo readWord.term)
		.let { argument ->
			function
				.get(argument)
				.let { matchOrNull ->
					if (matchOrNull == null)
						copy(termOrNull = null).readPreprocessed(token)
					else when (matchOrNull.bodyBinaryTrieMatch) {
						is BinaryTrie.Match.Partial ->
							this // partial - continue
						is BinaryTrie.Match.Full ->
							copy(termOrNull = matchOrNull.bodyBinaryTrieMatch.value.apply(argument)).termParse
					}
				}
		}

val TokenReader.termParse: TokenReader?
	get() =
		copy(termOrNull = null).orNull
			.fold(termOrNull?.fieldStreamOrNull?.reverse) { field ->
				if (this == null) null
				else if (termOrNull != null) termPush(field)
				else field.parseToken.let { tokenOrNull ->
					if (tokenOrNull == null) termPush(field)
					else readPreprocessed(tokenOrNull)
				}
			}


fun TokenReader.readPreprocessed(token: Token<Nothing>): TokenReader? =
	tokenEvaluator.evaluate(token)?.let { tokenEvaluator ->
		copy(tokenEvaluator = tokenEvaluator)
	}

// === bit stream

val TokenReader.bitStreamOrNull: Stream<Bit>?
	get() =
		tokenEvaluator.bitStreamOrNull

val TokenReader.function: Function
	get() =
		tokenEvaluator.function
