package leo14.typed

import leo14.lambda.scriptLine
import leo14.lineTo
import leo14.script

val <T> Typed<T>.reflectScriptLine get() =
	"value" lineTo script(term.scriptLine, type.scriptLine)

val <T> Function<T>.reflectScriptLine
	get() =
	"action" lineTo script(
		"param" lineTo takes.script,
		"body" lineTo script(does.reflectScriptLine))