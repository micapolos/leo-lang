package leo13.untyped.normalizer

import leo13.*
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.script.script
import leo13.token.*

data class Normalizer(
	val processor: Processor<Token>,
	val parentConverter: Converter<Normalizer, Token>,
	val initialProcessor: Processor<Token>,
	val tokenStack: Stack<Token>) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() = "normalizer" lineTo script(
			"parent" lineTo script(parentConverter.scriptingLine),
			processor.scriptingLine,
			"initial" lineTo script(initialProcessor.scriptingLine),
			"token" lineTo script("stack" lineTo tokenStack.scripting.script.emptyIfEmpty))

	override fun process(token: Token) =
		when (token) {
			is OpeningToken ->
				processor.process(token).normalizer(
					converter { childNormalizer ->
						if (childNormalizer.tokenStack.isEmpty)
							stack<Token>()
								.push(token)
								.pushAll(tokenStack)
								.push(token(closing))
								.let { normalizedTokenStack ->
									initialProcessor.processAll(normalizedTokenStack).normalizer(
										parentConverter,
										initialProcessor,
										normalizedTokenStack)
								}
						else
							childNormalizer.processor.process(token(closing))
								.normalizer(
									parentConverter,
									initialProcessor,
									tokenStack
										.push(token)
										.pushAll(childNormalizer.tokenStack)
										.push(token(closing)))
					})
			is ClosingToken ->
				parentConverter.convert(this)
		}
}

fun Processor<Token>.normalizer(
	parentConverter: Converter<Normalizer, Token> = converter { it.processor.process(token(closing)) },
	initialProcessor: Processor<Token> = this,
	tokenStack: Stack<Token> = stack()) =
	Normalizer(this, parentConverter, initialProcessor, tokenStack)
