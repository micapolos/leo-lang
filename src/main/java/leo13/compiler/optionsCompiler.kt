package leo13.compiler

import leo13.*
import leo13.type.Options
import leo13.type.lineTo
import leo13.type.type
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token

data class OptionsCompiler(
	val converter: Converter<Options, Token>,
	val context: TypeContext,
	val options: Options) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = compilerName lineTo script(
			converter.scriptingLine,
			context.scriptingLine,
			options.scriptingLine)

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken ->
				TypeCompiler(
					converter { type ->
						OptionsCompiler(
							converter,
							context,
							options.plus(context.definitions.resolve(token.opening.name lineTo type)))
					},
					false,
					context.trace(token.opening.name),
					type())
			is ClosingToken -> converter.convert(options)
		}
}