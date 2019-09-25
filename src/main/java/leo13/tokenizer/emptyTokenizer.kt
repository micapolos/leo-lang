package leo13.tokenizer

import leo13.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token

data class EmptyTokenizer(
	val converter: Converter<Unit, Token>) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			tokenizerName lineTo script(converter.scriptingLine)


	override fun process(token: Token) =
		when (token) {
			is OpeningToken -> tracedError(expectedName lineTo script(endName))
			is ClosingToken -> converter.convert(Unit)
		}
}
