package leo14.untyped

import leo14.Script

data class Doing(
	val scope: Scope,
	val script: Script)

fun doing(scope: Scope, script: Script) =
	Doing(scope, script)

fun doing(script: Script) = doing(scope(), script)

tailrec fun Doing.applyScript(given: Thunk): Thunk {
	// TODO: This code is repeated with Resolver.do_(). Extract it.
	val done = scope
		.push(given.scriptDefinition)
		.resolver()
		.evaluate(script)
		.thunk
	val repeatOrNull = done.matchPrefix(repeatName) { it }
	return if (repeatOrNull == null) done
	else applyScript(repeatOrNull)
}

tailrec fun Doing.with(given: Thunk): Thunk {
	// TODO: This code is repeated with Resolver.do_(). Extract it.
	val done = scope
		.bind(given)
		.resolver()
		.evaluate(script)
		.thunk
	val repeatOrNull = done.matchPrefix(repeatName) { it }
	return if (repeatOrNull == null) done
	else with(repeatOrNull)
}
