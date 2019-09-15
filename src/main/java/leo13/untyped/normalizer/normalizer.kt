package leo13.untyped.normalizer

import leo13.*
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.script.script
import leo13.token.*
import leo9.*

data class Normalizer(
	val parentConverter: Converter<Normalizer, Token>,
	val processor: Processor<Token>,
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
				normalizer(
					converter { childNormalizer ->
						if (childNormalizer.tokenStack.isEmpty)
							stack<Token>()
								.push(token)
								.pushAll(tokenStack)
								.push(token(closing))
								.let { normalizedTokenStack ->
									normalizer(
										parentConverter,
										initialProcessor.process(normalizedTokenStack),
										initialProcessor,
										normalizedTokenStack)
								}
						else
							normalizer(
								parentConverter,
								childNormalizer.processor.process(token(closing)),
								initialProcessor,
								tokenStack
									.push(token)
									.pushAll(childNormalizer.tokenStack)
									.push(token(closing)))
					},
					processor.process(token))
			is ClosingToken ->
				parentConverter.convert(this)
		}
}

fun normalizer(
	parentConverter: Converter<Normalizer, Token> = errorConverter(),
	processor: Processor<Token>,
	initialProcessor: Processor<Token> = processor,
	tokenStack: Stack<Token> = stack()) =
	Normalizer(parentConverter, processor, initialProcessor, tokenStack)
