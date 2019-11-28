package leo14.typed

import leo14.lambda.script
import leo14.lineTo
import leo14.script

val <T> Typed<T>.reflectScriptLine get() =
	"typed" lineTo script(
		"term" lineTo term.script,
		"type" lineTo type.script)

val <T> Action<T>.reflectScriptLine get() =
	"action" lineTo script(
		"param" lineTo param.script,
		"body" lineTo script(body.reflectScriptLine))