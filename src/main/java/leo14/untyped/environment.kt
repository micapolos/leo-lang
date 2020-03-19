package leo14.untyped

import leo14.Begin

sealed class Environment

data class ContextEnvironment(val context: Context) : Environment()
data class QuotedEnvironment(val unquote: Environment) : Environment()

val Context.environment: Environment get() = ContextEnvironment(this)
val Environment.quote: Environment get() = QuotedEnvironment(this)
val emptyEnvironment get() = context().environment

val Environment.unquoteOrNull: Environment?
	get() =
		(this as? QuotedEnvironment)?.unquote

fun Environment.writeEvaluator(sequence: Sequence) =
	when (this) {
		is ContextEnvironment ->
			context.resolver(sequence).evaluator
		is QuotedEnvironment ->
			evaluator(program(sequence))
	}

fun Environment.write(begin: Begin): Environment? =
	when (begin.string) {
		"quote" -> quote
		"unquote" -> unquoteOrNull
		else -> this
	}
