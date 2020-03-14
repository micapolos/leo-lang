package leo14.untyped

import leo14.ScriptLink

data class Rule(val pattern: Pattern, val body: Body)

fun Rule.resolve(scriptLink: ScriptLink) =
	if (pattern.matches(scriptLink)) body.apply(scriptLink)
	else null