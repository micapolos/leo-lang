package leo13.compiler

import leo13.*
import leo13.expression.caseTo
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.type.EmptyOptions
import leo13.type.LinkOptions
import leo13.type.Options
import leo13.type.type

data class SwitchCompiler(
	val converter: Converter<TypedSwitch, Token>,
	val context: Context,
	val remainingOptions: Options,
	val typed: TypedSwitch) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			compilerName lineTo script(
				converter.scriptingLine,
				context.scriptingLine,
				remainingName lineTo script(remainingOptions.scriptingLine),
				typed.scriptingLine)

	override fun process(token: Token) =
		when (token) {
			is OpeningToken -> begin(token.opening.name)
			is ClosingToken -> end
		}

	fun begin(name: String) =
		when (remainingOptions) {
			is EmptyOptions -> tracedError(notName lineTo script(expectedName lineTo script(name)))
			is LinkOptions ->
				remainingOptions.link.item.line.name.let { optionName ->
					if (optionName != name)
						tracedError(expectedName lineTo script(optionName))
					else Compiler(
						converter { typedRhs ->
							plus(typed(optionName caseTo typedRhs.expression, typedRhs.type))
								.copy(remainingOptions = this@SwitchCompiler.remainingOptions.link.lhs)
						},
						voidProcessor(),
						compiled(context.match(type(remainingOptions.link.item.line))))
				}
		}

	val end: Processor<Token>
		get() =
			when (remainingOptions) {
				is EmptyOptions -> converter.convert(typed)
				is LinkOptions -> tracedError(expectedName lineTo script(remainingOptions.link.item.line.name))
			}
}

fun switchCompiler(
	converter: Converter<TypedSwitch, Token> = errorConverter(),
	context: Context,
	remainingOptions: Options,
	typedSwitch: TypedSwitch) =
	SwitchCompiler(converter, context, remainingOptions, typedSwitch)

fun SwitchCompiler.plus(typedCase: TypedCase) =
	copy(
		typed = typed.plus(typedCase))
