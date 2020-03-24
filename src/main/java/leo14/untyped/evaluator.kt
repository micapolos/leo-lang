package leo14.untyped

import leo14.Begin

data class Evaluator(
	val environment: Environment,
	val thunk: Thunk)

val emptyEvaluator
	get() =
		emptyEnvironment.evaluator(thunk(value()))

fun Environment.evaluator(thunk: Thunk) =
	Evaluator(this, thunk)

val Resolver.evaluator
	get() =
		compiler.environment.evaluator(thunk)

fun Evaluator.write(line: Line): Evaluator =
	environment.writeEvaluator(thunk sequenceTo line)

fun Evaluator.write(begin: Begin): Evaluator? =
	environment.write(begin)?.evaluator(thunk(value()))

fun Evaluator.write(field: Field): Evaluator? =
	when (environment) {
		is ContextEnvironment ->
			when (field.name) {
				quoteName -> environment.evaluator(thunk.plus(field.thunk))
				else -> write(line(field))
			}
		is QuotedEnvironment ->
			when (field.name) {
				unquoteName ->
					if (environment.unquote is ContextEnvironment) environment.evaluator(thunk.plus(field.thunk))
					else write(line(field))
				else -> write(line(field))
			}
	}