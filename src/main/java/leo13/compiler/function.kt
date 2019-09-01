package leo13.compiler

import leo.base.notNullIf
import leo13.Scriptable
import leo13.script.lineTo
import leo13.script.script
import leo13.type.Type

data class Function(val parameter: Type, val body: Compiled) : Scriptable() {
	override val scriptableName get() = "function"
	override val scriptableBody
		get() = script(
			"parameter" lineTo script(parameter.scriptableLine),
			body.scriptableLine)
}

fun function(parameter: Type, body: Compiled) = Function(parameter, body)

fun Function.compiledOrNull(parameter: Type): Compiled? =
	notNullIf(this.parameter == parameter) { body }
