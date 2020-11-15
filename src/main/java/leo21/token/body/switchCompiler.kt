package leo21.token.body

import leo.base.println
import leo13.Stack
import leo13.linkOrNull
import leo13.push
import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo14.lambda.fn
import leo21.compiled.SwitchCompiled
import leo21.compiled.case
import leo21.compiled.compiled
import leo21.compiled.end
import leo21.compiled.of
import leo21.token.processor.BodyCompilerTokenProcessor
import leo21.token.processor.SwitchCompilerTokenProcessor
import leo21.token.processor.TokenProcessor
import leo21.type.Field
import leo21.type.Line
import leo21.type.arrowTo
import leo21.type.choice
import leo21.type.fieldTo
import leo21.type.line
import leo21.type.name
import leo21.type.type

data class SwitchCompiler(
	val parentBodyCompiler: BodyCompiler,
	val module: Module,
	val caseFieldStack: Stack<Field>,
	val switchCompiled: SwitchCompiled
)

fun SwitchCompiler.plus(token: Token): TokenProcessor =
	when (token) {
		is LiteralToken -> null
		is BeginToken ->
			switchCompiled.remainingLineStack.linkOrNull { choice }?.let { choiceLink ->
				leo.base.notNullIf(token.begin.string == choiceLink.line.name) {
					BodyCompilerTokenProcessor(
						BodyCompiler(
							BodyCompiler.Parent.SwitchCase(this, choiceLink.line),
							module.begin(type(choiceLink.line).asGiven).body(compiled())))
				}
			}
		is EndToken -> BodyCompilerTokenProcessor(parentBodyCompiler.set(switchCompiled.end))
	}!!

fun SwitchCompiler.plus(line: Line, body: Body): TokenProcessor =
	SwitchCompilerTokenProcessor(
		copy(
			switchCompiled = switchCompiled.case(line.name,
				fn(body.compiled.term).of(type(line) arrowTo body.compiled.type)),
			caseFieldStack = caseFieldStack.push(line.name fieldTo body.compiled.type)))