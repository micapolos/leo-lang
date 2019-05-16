package leo3

import leo.base.*

data class TokenReader(
	val parentOrNull: TokenReaderParent?,
	val termOrNull: Term?) {
	override fun toString() = appendableString { it.append(this) }
}

data class TokenReaderParent(
	val tokenReader: TokenReader,
	val begin: Begin) {
	override fun toString() = appendableString { it.append(this) }
}

val Empty.tokenReader
	get() = TokenReader(null, null)

fun TokenReader.plus(token: Token): TokenReader? =
	when (token) {
		is BeginToken -> TokenReader(TokenReaderParent(this, token.begin), null)
		is EndToken -> parentOrNull?.let { parent ->
			TokenReader(
				parent.tokenReader.parentOrNull,
				parent.tokenReader.termOrNull.plus(parent.begin.word, termOrNull))
		}
	}

fun TokenReader.plus(termOrNull: Term?): TokenReader =
	orNullFold(termOrNull.tokenSeq, TokenReader::plus)!!

val TokenReader.parameterOrNull: Parameter?
	get() = parentOrNull.ifNull { parameter(termOrNull) }

fun Appendable.append(tokenReader: TokenReader) =
	this
		.ifNotNull(tokenReader.parentOrNull) { append(it) }
		.ifNotNull(tokenReader.termOrNull) { append(it) }

fun Appendable.append(tokenReaderParent: TokenReaderParent): Appendable =
	this
		.append(tokenReaderParent.tokenReader)
		.append(tokenReaderParent.begin)
