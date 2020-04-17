package leo15.type

import leo14.ScriptLine
import leo14.invoke
import leo15.string

data class Scoped(val scope: Scope, val typed: Typed) {
	override fun toString() = reflectScriptLine.string
}

infix fun Scope.with(typed: Typed) = Scoped(this, typed)

val Scoped.reflectScriptLine: ScriptLine
	get() =
		"scoped"(scope.reflectScriptLine, typed.reflectScriptLine)