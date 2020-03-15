package leo14.untyped

import leo14.ScriptLink
import leo14.lineTo
import leo14.link
import leo14.script

data class Pattern(val scriptLink: ScriptLink)

fun Pattern.matches(scriptLink: ScriptLink) =
	this.scriptLink.matches(scriptLink)

val givenPattern =
	Pattern(link("given" lineTo script()))
