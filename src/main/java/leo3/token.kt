package leo3

import leo.base.appendableString
import leo.base.nullOf

sealed class Token

data class BeginToken(val begin: Begin) : Token() {
	override fun toString() = appendableString { it.append(this) }
}

data class EndToken(val end: End) : Token() {
	override fun toString() = appendableString { it.append(this) }
}

fun token(begin: Begin): Token = BeginToken(begin)
fun token(end: End): Token = EndToken(end)

fun token(wordOrNull: Word?) =
	if (wordOrNull == null) token(end) else token(begin(wordOrNull))

val Token.bitSeq
	get() = when (this) {
		is BeginToken -> begin.bitSeq
		is EndToken -> end.bitSeq
	}

fun Appendable.append(token: Token) =
	when (token) {
		is BeginToken -> append(token.begin)
		is EndToken -> append(token.end)
	}

fun Reader.readToken(): Read<Token> =
	readByte().let { readByte ->
		nullOf<Word>()
			.plus(readByte.value)
			?.let { word -> readByte.reader.readWordTo(word).map { token(begin(it)) } }
			?: readByte.map { token(end) }
	}
