package leo3

import leo.base.*
import leo.binary.Bit

data class TokenReader(
	val lineReader: LineReader,
	val parentOrNull: TokenReaderParent?) {
	override fun toString() = appendableString { it.append(this) }
}

data class TokenReaderParent(
	val tokenReader: TokenReader,
	val begin: Begin) {
	override fun toString() = appendableString { it.append(this) }
}

val LineReader.completedTokenReader
	get() = TokenReader(this, null)

val TokenReader.isCompleted
	get() = parentOrNull == null

val TokenReader.completedLineReaderOrNull
	get() = notNullIf(isCompleted) { lineReader }

fun TokenReader.plus(token: Token): TokenReader? =
	when (token) {
		is BeginToken -> TokenReader(lineReader.begin, TokenReaderParent(this, token.begin))
		is EndToken -> parentOrNull?.let { parent ->
			TokenReader(
				parent.tokenReader.lineReader.plus(line(parent.begin.word, lineReader.value)),
				parent.tokenReader.parentOrNull)
		}
	}

fun Appendable.append(tokenReader: TokenReader) =
	this
		.ifNotNull(tokenReader.parentOrNull) { append(it) }
		.append(tokenReader.lineReader)

fun Appendable.append(tokenReaderParent: TokenReaderParent): Appendable =
	this
		.append(tokenReaderParent.tokenReader)
		.append(tokenReaderParent.begin)

val TokenReader.bitSeq: Seq<Bit>
	get() = Seq { seqNodeOrNull(lineReader.bitSeq) }