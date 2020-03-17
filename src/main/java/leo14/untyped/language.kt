package leo14.untyped

import leo14.*

data class Leo(
	val parentOrNull: LeoParent?,
	val fragment: Fragment,
	val environment: Environment)

data class LeoParent(
	val leo: Leo,
	val op: LeoOp)

sealed class LeoOp
data class PushNamesLeoOp(val name: String) : LeoOp()
object PushLeoOp : LeoOp()

fun Leo.append(token: Token): Leo? =
	when (token) {
		is LiteralToken -> append(token.literal)
		is BeginToken -> begin(token.begin.string)
		is EndToken -> end()
	}

fun Leo.begin(name: String): Leo? =
	when (environment) {
		is ContextEnvironment ->
			when (name) {
				"quote" ->
					Leo(
						LeoParent(this, PushLeoOp),
						fragment.begin(name),
						environment.quoted.environment)
				"unquote" ->
					null
				else ->
					Leo(
						LeoParent(this, PushNamesLeoOp(name)),
						fragment.begin(name),
						environment)
			}
		is QuotedEnvironment ->
			when (name) {
				"quote" ->
					Leo(
						LeoParent(this, PushNamesLeoOp(name)),
						fragment.begin(name),
						environment.quoted.environment)
				"unquote" ->
					Leo(
						LeoParent(this, PushNamesLeoOp(name)),
						fragment,
						environment.quoted.environment)
				else -> Leo(
					LeoParent(this, PushNamesLeoOp(name)),
					fragment.begin(name),
					environment)
			}
	}

fun Leo.append(literal: Literal): Leo =
	when (environment) {
		is ContextEnvironment ->
			environment.context.resolver(fragment.program).apply(value(literal)).let { resolver ->
				Leo(
					parentOrNull,
					Fragment(fragment.parentOrNull, resolver.program),
					resolver.context.environment)
			}
		is QuotedEnvironment ->
			Leo(
				parentOrNull,
				fragment.append(value(literal)),
				environment)
	}

fun Leo.end(): Leo? =
	parentOrNull?.let { parent ->
		when (parent.op) {
			is PushNamesLeoOp -> TODO()
			PushLeoOp ->
				Leo(
					parent.leo.parentOrNull,
					parent.leo.fragment.updateProgram { plus(fragment.program) },
					parent.leo.environment)
		}
	}
