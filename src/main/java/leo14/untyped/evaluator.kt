package leo14.untyped

import leo14.Begin

data class Evaluator(
	val environment: Environment,
	val value: Value)

val emptyEvaluator
	get() =
		emptyEnvironment.evaluator(value())

fun Environment.evaluator(value: Value) =
	Evaluator(this, value)

val Resolver.evaluator
	get() =
		compiler.environment.evaluator(value)

fun Evaluator.write(line: Line): Evaluator =
	environment.writeEvaluator(value sequenceTo line)

fun Evaluator.write(begin: Begin): Evaluator? =
	environment.write(begin)?.evaluator(value())

fun Evaluator.write(field: Field): Evaluator? =
	when (environment) {
		is ContextEnvironment ->
			when (field.name) {
				quoteName -> environment.evaluator(value.plus(field.rhs))
				else -> write(line(field))
			}
		is QuotedEnvironment ->
			when (field.name) {
				unquoteName ->
					if (environment.unquote is ContextEnvironment) environment.evaluator(value.plus(field.rhs))
					else write(line(field))
				else -> write(line(field))
			}
	}