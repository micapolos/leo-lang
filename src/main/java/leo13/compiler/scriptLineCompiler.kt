package leo13.compiler

import leo13.*
import leo13.script.*
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token

data class ScriptLineCompiler(
	val converter: Converter<ScriptLine, Token>,
	val line: ScriptLine): ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine get() =
		compilerName lineTo script(converter.scriptingLine, line.scriptableLine)

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken -> ScriptLineCompiler(
				converter { ScriptLineCompiler(converter, line.name lineTo line.rhs.plus(it)) },
				token.opening.name.scriptLine)
			is ClosingToken -> converter.convert(line)
		}
}
