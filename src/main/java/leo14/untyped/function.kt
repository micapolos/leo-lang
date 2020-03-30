package leo14.untyped

import leo14.Script

data class Function(
	val scope: Scope,
	val script: Script)

fun function(scope: Scope, script: Script) =
	Function(scope, script)

fun function(script: Script) = function(scope(), script)

tailrec fun Function.apply(given: Thunk): Thunk {
	// TODO: This code is repeated with Resolver.do_(). Extract it.
	val done = scope
		.push(given.givenRule)
		.resolver()
		.evaluate(script)
		.thunk
	val repeatOrNull = done.matchPostfix(repeatName) { it }
	return if (repeatOrNull == null) done
	else apply(repeatOrNull)
}
