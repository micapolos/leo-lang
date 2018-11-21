package leo

import leo.base.*

data class TokenReader(
	val tokenEvaluator: TokenEvaluator,
	val termOrNull: Term<Nothing>?)

val emptyTokenReader =
	TokenReader(emptyTokenEvaluator, null)

fun TokenReader.read(token: Token<Nothing>): TokenReader? =
	if (!tokenReaderEnabled) readPreprocessed(token)
	else this
		.termPush(leoReadField(token))
		.termInvoke
		.termParse

fun TokenReader.termPush(field: Field<Nothing>): TokenReader =
	copy(termOrNull = termOrNull.push(field))

val TokenReader.termInvoke: TokenReader
	get() =
		if (termOrNull == null) this
		else copy(termOrNull = function.apply(termOrNull)?.value ?: termOrNull)

val TokenReader.termParse: TokenReader?
	get() =
		copy(termOrNull = null).orNull
			.fold(termOrNull?.fieldStreamOrNull?.reverse) { field ->
				if (this == null) null
				else if (termOrNull != null) termPush(field)
				else if (field == leoWord fieldTo continueWord.term) this
				else field.leoReadTokenOrNull.let { token ->
					if (token == null) termPush(field)
					else readPreprocessed(token)
				}
			}

fun TokenReader.readPreprocessed(token: Token<Nothing>): TokenReader? =
	tokenEvaluator.evaluate(token)?.let { tokenEvaluator ->
		copy(tokenEvaluator = tokenEvaluator)
	}

// === leo read bit

fun leoReadField(token: Token<Nothing>): Field<Nothing> =
	leoWord fieldTo term(readWord fieldTo token.reflect.term)

val Field<Nothing>.leoReadTokenOrNull: Token<Nothing>?
	get() =
		get(leoWord)?.let { theLeoTerm ->
			theLeoTerm.value?.match(readWord) { readTerm ->
				readTerm?.match(tokenWord) { tokenTerm ->
					tokenWord.fieldTo(tokenTerm).parseToken
				}
			}
		}

// === bit stream

val TokenReader.bitStreamOrNull: Stream<Bit>?
	get() =
		tokenEvaluator.bitStreamOrNull

val TokenReader.function: Function
	get() =
		tokenEvaluator.function
