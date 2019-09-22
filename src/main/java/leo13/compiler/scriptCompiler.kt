package leo13.compiler

import leo13.*
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.plus
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token

data class ScriptCompiler(
	val converter: Converter<Script, Token>,
	val script: Script): ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine get() = compilerName lineTo script(
		converter.scriptingLine,
		script.scriptableLine)


	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken -> ScriptCompiler(
				converter { ScriptCompiler(converter, script.plus(token.opening.name lineTo it))},
				script())
			is ClosingToken -> converter.convert(script)
		}
}