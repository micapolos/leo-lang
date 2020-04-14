package leo15

import leo14.ScriptLine
import leo14.lineTo
import leo14.script

data class Compiled(val scope: Scope, val typed: Typed) {
	override fun toString() = reflectScriptLine.string
}

fun Scope.compiled(typed: Typed) = Compiled(this, typed)

val Compiled.reflectScriptLine: ScriptLine
	get() =
		"compiled" lineTo script(scope.reflectScriptLine, typed.reflectScriptLine)