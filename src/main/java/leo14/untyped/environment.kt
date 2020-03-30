package leo14.untyped

import leo14.Begin

sealed class Environment

data class ContextEnvironment(val scope: Scope) : Environment()
data class QuotedEnvironment(val unquote: Environment) : Environment()

val Scope.environment: Environment get() = ContextEnvironment(this)
val Environment.quote: Environment get() = QuotedEnvironment(this)
val emptyEnvironment get() = scope().environment

val Environment.unquoteOrNull: Environment?
	get() =
		(this as? QuotedEnvironment)?.unquote

fun Environment.writeEvaluator(sequence: Sequence) =
	when (this) {
		is ContextEnvironment ->
			scope.resolver(sequence).evaluator
		is QuotedEnvironment ->
			evaluator(thunk(value(sequence)))
	}

fun Environment.write(begin: Begin): Environment? =
	when (begin.string) {
		quoteName -> quote
		unquoteName -> unquoteOrNull
		else -> this
	}
