package leo13.compiler

import leo.base.notNullIf
import leo13.script.Scriptable
import leo13.script.lineTo
import leo13.script.script

data class Function(val parameter: Trace, val body: Compiled) : Scriptable() {
	override val scriptableName get() = "function"
	override val scriptableBody
		get() = script(
			"parameter" lineTo script(parameter.scriptableLine),
			body.scriptableLine)
}

fun function(parameter: Trace, body: Compiled) = Function(parameter, body)

fun Function.compiledOrNull(parameter: Trace): Compiled? =
	notNullIf(this.parameter == parameter) { body }
