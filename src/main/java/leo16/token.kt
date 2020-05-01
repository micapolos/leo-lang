package leo16

import leo.base.*
import leo13.seq
import leo14.LiteralToken
import leo15.beginName
import leo15.endName
import leo15.tokenName

sealed class Token {
	override fun toString() = asSentence.toString()
}

data class BeginToken(val word: String) : Token() {
	override fun toString() = super.toString()
}

object EndToken : Token()

val String.beginToken: Token get() = BeginToken(this)
val endToken: Token = EndToken

val Token.asSentence: Sentence
	get() =
		tokenName(
			when (this) {
				is BeginToken -> beginName(word())
				EndToken -> endName()
			})

val leo14.Token.tokenSeq: Seq<Token>
	get() =
		when (this) {
			is LiteralToken -> literal.expandSentence.tokenSeq
			is leo14.BeginToken -> begin.string.beginToken.onlySeq
			is leo14.EndToken -> endToken.onlySeq
		}

val Script.tokenSeq: Seq<Token>
	get() =
		sentenceStack.seq.reverse.mapFlat { tokenSeq }

val Sentence.tokenSeq: Seq<Token>
	get() =
		seq { word.beginToken.then(script.tokenSeq) }.thenFn { endToken.onlySeq }