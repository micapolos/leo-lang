package leo14.untyped

import leo14.Script

data class Action(
	val scope: Scope,
	val script: Script)

fun action(scope: Scope, script: Script) =
	Action(scope, script)

fun action(script: Script) = action(scope(), script)

fun Action.pushScriptAndApply(given: Thunk): Thunk =
	fix(given) { push(it.scriptDefinition) }

fun Action.bindAndApply(given: Thunk): Thunk =
	fix(given) { bind(it) }

tailrec fun Action.fix(given: Thunk, bind: Scope.(Thunk) -> Scope): Thunk {
	val done = scope
		.push(definition(recurse(this)))
		.bind(given)
		.resolver()
		.evaluate(script)
		.thunk
	val repeatOrNull = done.matchPrefix(repeatName) { it }
	return if (repeatOrNull == null) done
	else fix(repeatOrNull, bind)
}
