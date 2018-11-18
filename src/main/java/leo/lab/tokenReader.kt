package leo.lab

import leo.base.Bit
import leo.base.Stream
import leo.base.fold
import leo.base.orNull
import leo.continueWord
import leo.leoWord
import leo.readWord
import leo.tokenWord

data class TokenReader(
	val tokenEvaluator: TokenEvaluator,
	val termOrNull: Term<Nothing>?)

val emptyTokenReader =
	TokenReader(emptyTokenEvaluator, null)

fun TokenReader.read(token: Token): TokenReader? =
	this
		.termPush(leoReadField(token))
		.termInvoke
		.termParse

fun TokenReader.termPush(field: Field<Nothing>): TokenReader =
	copy(termOrNull = termOrNull.push(field))

val TokenReader.termInvoke: TokenReader
	get() =
		if (termOrNull == null) this
		else copy(termOrNull = invoke(termOrNull))

val TokenReader.termParse: TokenReader?
	get() =
		copy(termOrNull = null).orNull
			.fold(termOrNull?.fieldStreamOrNull) { field ->
				if (this == null) null
				else if (termOrNull != null) termPush(field)
				else if (field == leoWord fieldTo continueWord.term) this
				else field.leoReadTokenOrNull.let { token ->
					if (token == null) termPush(field)
					else readPreprocessed(token)
				}
			}

fun TokenReader.readPreprocessed(token: Token): TokenReader? =
	tokenEvaluator.evaluate(token)?.let { tokenEvaluator ->
		copy(tokenEvaluator = tokenEvaluator)
	}

// === leo read bit

fun leoReadField(token: Token): Field<Nothing> =
	leoWord fieldTo term(readWord fieldTo token.reflect.term)

val Field<Nothing>.leoReadTokenOrNull: Token?
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

fun TokenReader.invoke(term: Term<Nothing>) =
	tokenEvaluator.invoke(term)