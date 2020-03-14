package leo14.untyped

import leo14.*

data class Body(val script: Script, val contextOrNull: Context?)

fun Body.apply(scriptLink: ScriptLink) =
	if (contextOrNull == null) script
	else contextOrNull
		.push(
			Rule(
				Pattern(link("given" lineTo script())),
				Body(script("given" lineTo script(scriptLink)), null)))
		.eval(script)
