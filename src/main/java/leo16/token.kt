package leo16

import leo15.beginName
import leo15.endName
import leo15.tokenName

sealed class Token {
	override fun toString() = sentence.toString()
}

data class BeginToken(val word: String) : Token() {
	override fun toString() = super.toString()
}

object EndToken : Token()

val String.beginToken: Token get() = BeginToken(this)
val endToken: Token = EndToken

val Token.sentence: Sentence
	get() =
		tokenName(
			when (this) {
				is BeginToken -> beginName(word())
				EndToken -> endName()
			})
