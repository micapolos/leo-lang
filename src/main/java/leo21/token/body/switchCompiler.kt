package leo21.token.body

import leo13.Stack
import leo13.linkOrNull
import leo13.push
import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo14.error
import leo14.lambda.fn
import leo14.orError
import leo15.dsl.*
import leo21.compiled.SwitchCompiled
import leo21.compiled.case
import leo21.compiled.compiled
import leo21.compiled.end
import leo21.compiled.of
import leo21.token.processor.BodyCompilerProcessor
import leo21.token.processor.SwitchCompilerProcessor
import leo21.token.processor.Processor
import leo21.type.Field
import leo21.type.Line
import leo21.type.arrowTo
import leo21.type.choice
import leo21.type.fieldTo
import leo21.type.line
import leo21.type.name
import leo21.type.type

// TODO: This class is terrible. Refactor without using SwitchCompiled.

data class SwitchCompiler(
	val parentBodyCompiler: BodyCompiler,
	val module: Module,
	val caseFieldStack: Stack<Field>,
	val switchCompiled: SwitchCompiled
)

fun SwitchCompiler.plus(token: Token): Processor =
	when (token) {
		is LiteralToken -> error { not { expected { literal } } }
		is BeginToken ->
			switchCompiled
				.remainingLineStack
				.linkOrNull { choice }
				.orError { expected { choice } }
				.let { choiceLink ->
					if (token.begin.string == choiceLink.line.name)
						BodyCompilerProcessor(
							BodyCompiler(
								BodyCompiler.Parent.SwitchCase(this, choiceLink.line),
								module.begin(type(choiceLink.line).given).body(compiled())))
					else error { expected { case { x(choiceLink.line.name) } } }
				}
		is EndToken -> BodyCompilerProcessor(parentBodyCompiler.set(switchCompiled.end))
	}

fun SwitchCompiler.plus(line: Line, body: Body): Processor =
	SwitchCompilerProcessor(
		copy(
			switchCompiled = switchCompiled.case(line.name,
				fn(body.compiled.term).of(type(line) arrowTo body.compiled.type)),
			caseFieldStack = caseFieldStack.push(line.name fieldTo body.compiled.type)))