package leo13.compiler

import leo13.*
import leo13.expression.caseTo
import leo13.pattern.Choice
import leo13.pattern.EmptyChoice
import leo13.pattern.LinkChoice
import leo13.pattern.pattern
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token

data class SwitchCompiler(
	val converter: Converter<SwitchCompiled, Token>,
	val context: Context,
	val remainingChoice: Choice,
	val compiled: SwitchCompiled) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			"compiler" lineTo script(
				converter.scriptingLine,
				context.scriptingLine,
				"remaining" lineTo script(remainingChoice.scriptingLine),
				compiled.scriptingLine)

	override fun process(token: Token) =
		when (token) {
			is OpeningToken -> begin(token.opening.name)
			is ClosingToken -> end
		}

	fun begin(name: String) =
		when (remainingChoice) {
			is EmptyChoice -> tracedError("exhausted" lineTo script("switch"))
			is LinkChoice ->
				remainingChoice.link.line.name.let { choiceName ->
					if (choiceName != name)
						tracedError("expected" lineTo script(choiceName))
					else compiler(
						converter { rhsCompiled ->
							plus(compiled(choiceName caseTo rhsCompiled.expression, rhsCompiled.pattern))
								.copy(remainingChoice = this@SwitchCompiler.remainingChoice.link.lhs)
						},
						context.switch(pattern(remainingChoice.link.line)))
				}
		}

	val end: Processor<Token>
		get() =
			when (remainingChoice) {
				is EmptyChoice -> converter.convert(compiled)
				is LinkChoice -> tracedError("expected" lineTo script(remainingChoice.link.line.name))
			}
}

fun switchCompiler(
	converter: Converter<SwitchCompiled, Token> = errorConverter(),
	context: Context,
	remainingChoice: Choice,
	switch: SwitchCompiled) =
	SwitchCompiler(converter, context, remainingChoice, switch)

fun SwitchCompiler.plus(case: CaseCompiled) =
	copy(
		compiled = compiled.plus(case))
