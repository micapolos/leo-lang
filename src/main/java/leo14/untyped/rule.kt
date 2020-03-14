package leo14.untyped

import leo14.Script
import leo14.ScriptLink
import leo14.lineTo
import leo14.script

data class Rule(val pattern: Pattern, val body: Body)

fun Rule.resolve(scriptLink: ScriptLink) =
	if (pattern.matches(scriptLink)) body.apply(scriptLink)
	else null

val Script.givenRule
	get() =
		Rule(givenPattern, body(script("given" lineTo this)))
