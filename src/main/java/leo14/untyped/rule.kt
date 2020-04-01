package leo14.untyped

import leo.base.notNullIf

data class Rule(val pattern: Pattern, val body: Body)

fun rule(pattern: Pattern, body: Body) = Rule(pattern, body)

fun Rule.apply(scope: Scope, given: Thunk): Applied? =
	notNullIf(pattern.matches(given)) {
		body.apply(scope, given)
	}

val Thunk.scriptBinding
	get() =
		thunk(value(scriptName)).bindingTo(thunk(value(scriptName lineTo this)))

val Thunk.scriptDefinition
	get() =
		definition(scriptBinding)
