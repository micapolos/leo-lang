package leo13.compiler

import leo13.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token

data class LineCompiler(
	val converter: Converter<Stack<TypedExpressionLine>, Token>,
	val context: Context,
	val typedExpression: Stack<TypedExpressionLine>
) :
	ObjectScripting(),
	Processor<Token> {
	override val scriptingLine
		get() =
			compilerName lineTo script(
				converter.scriptingLine,
				context.scriptingLine,
				listName lineTo script(typedExpression.scripting.scriptingLine))

	override fun process(token: Token) =
		when (token) {
			is OpeningToken -> begin(token.opening.name)
			is ClosingToken -> end
		}

	fun begin(name: String): Processor<Token> =
		compiler(
			converter { plus(name lineTo it) },
			context)

	val end: Processor<Token>
		get() =
			converter.convert(typedExpression)

	fun plus(line: TypedExpressionLine) =
		copy(typedExpression = typedExpression.push(context.typeLines.resolve(line)))
}

fun lineCompiler(
	converter: Converter<Stack<TypedExpressionLine>, Token>,
	context: Context,
	typedExpressionLineStack: Stack<TypedExpressionLine> = stack()) =
	LineCompiler(converter, context, typedExpressionLineStack)
