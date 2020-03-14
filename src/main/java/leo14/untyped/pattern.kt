package leo14.untyped

import leo14.ScriptLink

data class Pattern(val scriptLink: ScriptLink)

fun Pattern.matches(scriptLink: ScriptLink) =
	this.scriptLink.matches(scriptLink)