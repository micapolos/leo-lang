package leo14.untyped

data class Evaluator(
	val environment: Environment,
	val program: Program)

fun Environment.evaluator(program: Program) =
	Evaluator(this, program)

val Resolver.evaluator
	get() =
		context.environment.evaluator(program)

fun Evaluator.write(value: Value): Evaluator =
	environment.writeEvaluator(program sequenceTo value)