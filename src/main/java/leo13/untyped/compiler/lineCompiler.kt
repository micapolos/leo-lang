package leo13.untyped.compiler

import leo13.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo9.Stack
import leo9.push
import leo9.stack

data class LineCompiler(
	val converter: Converter<Stack<CompiledLine>, Token>,
	val context: Context,
	val compiled: Stack<CompiledLine>
) :
	ObjectScripting(),
	Processor<Token> {
	override val scriptingLine
		get() =
			"compiler" lineTo script(
				converter.scriptingLine,
				context.scriptingLine,
				"stack" lineTo script(compiled.scripting.scriptingLine))

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
			converter.convert(compiled)

	fun plus(line: CompiledLine) =
		copy(compiled = compiled.push(line))
}

fun lineCompiler(
	converter: Converter<Stack<CompiledLine>, Token>,
	context: Context,
	compiledLineStack: Stack<CompiledLine> = stack()) =
	LineCompiler(converter, context, compiledLineStack)
