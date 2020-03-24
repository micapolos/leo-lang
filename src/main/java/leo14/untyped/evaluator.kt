package leo14.untyped

import leo14.Begin

data class Evaluator(
	val environment: Environment,
	val program: Program)

val emptyEvaluator
	get() =
		emptyEnvironment.evaluator(program())

fun Environment.evaluator(program: Program) =
	Evaluator(this, program)

val Resolver.evaluator
	get() =
		compiler.environment.evaluator(program)

fun Evaluator.write(line: Line): Evaluator =
	environment.writeEvaluator(program sequenceTo line)

fun Evaluator.write(begin: Begin): Evaluator? =
	environment.write(begin)?.evaluator(program())

fun Evaluator.write(field: Field): Evaluator? =
	when (environment) {
		is ContextEnvironment ->
			when (field.name) {
				quoteName -> environment.evaluator(program.plus(field.rhs))
				else -> write(line(field))
			}
		is QuotedEnvironment ->
			when (field.name) {
				unquoteName ->
					if (environment.unquote is ContextEnvironment) environment.evaluator(program.plus(field.rhs))
					else write(line(field))
				else -> write(line(field))
			}
	}