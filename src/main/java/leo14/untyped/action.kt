package leo14.untyped

import leo14.Script

data class Action(
	val scope: Scope,
	val script: Script)

fun action(scope: Scope, script: Script) =
	Action(scope, script)

fun action(script: Script) = action(scope(), script)

tailrec fun Action.applyScript(given: Thunk): Thunk {
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

tailrec fun Action.with(given: Thunk): Thunk {
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

tailrec fun Action.fix(given: Thunk, bind: Scope.(Thunk) -> Scope): Thunk {
	val done = scope
		.bind(given)
		.resolver()
		.evaluate(script)
		.thunk
	val repeatOrNull = done.matchPrefix(repeatName) { it }
	return if (repeatOrNull == null) done
	else fix(repeatOrNull, bind)
}

