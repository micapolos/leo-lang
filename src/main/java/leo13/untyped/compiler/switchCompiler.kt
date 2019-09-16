package leo13.untyped.compiler

import leo13.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.untyped.pattern.Either
import leo9.Stack
import leo9.linkOrNull

data class SwitchCompiler(
	val converter: Converter<SwitchCompiled, Token>,
	val context: Context,
	val remainingEitherStack: Stack<Either>,
	val compiled: SwitchCompiled) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			"compiler" lineTo script(
				converter.scriptingLine,
				context.scriptingLine,
				"remaining" lineTo remainingEitherStack.scripting.script,
				compiled.scriptingLine)

	override fun process(token: Token) =
		when (token) {
			is OpeningToken -> begin(token.opening.name)
			is ClosingToken -> end
		}

	fun begin(name: String) =
		if (name != "case") tracedError("expected" lineTo script("case"))
		else remainingEitherStack
			.linkOrNull
			?.let { eitherStackLink ->
				caseCompiler(
					converter { caseCompiled ->
						switchCompiler(converter, context, eitherStackLink.stack, compiled.plus(caseCompiled))
					},
					context,
					eitherStackLink.value)
			}
			?: tracedError("missing" lineTo script("either"))

	val end get() = converter.convert(compiled)
}

fun switchCompiler(
	converter: Converter<SwitchCompiled, Token> = errorConverter(),
	context: Context,
	remainingEitherStack: Stack<Either>,
	switch: SwitchCompiled) =
	SwitchCompiler(converter, context, remainingEitherStack, switch)

fun SwitchCompiler.plus(case: CaseCompiled) =
	copy(
		compiled = compiled.plus(case))
