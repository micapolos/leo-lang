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
	val processor: Processor<Script>,
	val script: Script): ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine get() = compilerName lineTo script(
		converter.scriptingLine,
		script.scriptableLine)

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken -> ScriptCompiler(
				converter {
					script
						.plus(token.opening.name lineTo it)
						.let { script -> ScriptCompiler(converter, processor.process(script), script) }
				},
				voidProcessor(),
				script())
			is ClosingToken -> converter.convert(script)
		}
}

val Processor<Script>.scriptTokenProcessor: Processor<Token> get() =
	ScriptCompiler(errorConverter(), this, script())
