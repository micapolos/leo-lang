package leo13.type

import leo.base.notNullIf
import leo13.script.Scriptable
import leo13.script.lineTo
import leo13.script.script

data class Function(val parameter: Type, val body: Typed) : Scriptable() {
	override val scriptableName get() = "function"
	override val scriptableBody
		get() = script(
			"parameter" lineTo script(parameter.scriptableLine),
			body.scriptableLine)
}

fun function(parameter: Type, body: Typed) = Function(parameter, body)

fun Function.bodyOrNull(parameter: Type): Typed? =
	notNullIf(this.parameter == parameter) { body }
