package leo13.untyped.compiler

import leo13.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.untyped.expression.caseTo
import leo13.untyped.pattern.Either
import leo13.untyped.pattern.lineTo
import leo13.untyped.pattern.pattern

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
		remainingEitherStack
			.linkOrNull
			?.let { eitherStackLink ->
				if (eitherStackLink.value.name != name) tracedError("expected" lineTo script(eitherStackLink.value.name))
				else compiler(
					converter { rhsCompiled ->
						plus(compiled(name caseTo rhsCompiled.expression, rhsCompiled.pattern))
							.copy(remainingEitherStack = eitherStackLink.stack)
					},
					context.switch(pattern(eitherStackLink.value.name lineTo eitherStackLink.value.rhs)))
			}
			?: tracedError("exhausted" lineTo script("switch"))

	val end: Processor<Token>
		get() =
			remainingEitherStack
				.linkOrNull
				?.let { tracedError<Processor<Token>>("expected" lineTo script(it.value.name)) }
				?: converter.convert(compiled)
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
