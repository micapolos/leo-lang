package leo14.typed

import leo14.ScriptLine
import leo14.lambda.scriptLine
import leo14.lineTo
import leo14.script

fun <T> Typed<T>.reflectScriptLine(nativeFn: T.() -> ScriptLine) =
	"value" lineTo script(term.scriptLine(nativeFn), type.scriptLine)

fun <T> Function<T>.reflectScriptLine(nativeFn: T.() -> ScriptLine) =
	"action" lineTo script(
		"param" lineTo takes.script,
		"body" lineTo script(does.reflectScriptLine(nativeFn)))