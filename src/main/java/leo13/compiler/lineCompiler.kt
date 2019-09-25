package leo13.compiler

import leo13.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token

data class LineCompiler(
	val converter: Converter<Stack<ExpressionTypedLine>, Token>,
	val context: Context,
	val lineStack: Stack<ExpressionTypedLine>
) :
	ObjectScripting(),
	Processor<Token> {
	override val scriptingLine
		get() =
			compilerName lineTo script(
				converter.scriptingLine,
				context.scriptingLine,
				listName lineTo script(lineStack.scripting.scriptingLine))

	override fun process(token: Token) =
		when (token) {
			is OpeningToken -> begin(token.opening.name)
			is ClosingToken -> end
		}

	fun begin(name: String): Processor<Token> =
		Compiler(
			converter { plus(name lineTo it) },
			voidProcessor(),
			compiled(context))

	val end: Processor<Token>
		get() =
			converter.convert(lineStack)

	fun plus(line: ExpressionTypedLine) =
		copy(lineStack = lineStack.push(context.typeLines.resolve(line)))
}

fun lineCompiler(
	converter: Converter<Stack<ExpressionTypedLine>, Token>,
	context: Context,
	typedExpressionLineStack: Stack<ExpressionTypedLine> = stack()) =
	LineCompiler(converter, context, typedExpressionLineStack)
