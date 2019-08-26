package leo13.type

import leo.base.notNullIf
import leo13.script.Scriptable
import leo13.script.lineTo
import leo13.script.script

data class Function(val parameterType: Type, val typed: Typed) : Scriptable() {
	override val scriptableName get() = "function"
	override val scriptableBody
		get() = script(
			"parameter" lineTo script(parameterType.scriptableLine),
			typed.scriptableLine)
}

fun function(parameterType: Type, typed: Typed) = Function(parameterType, typed)

fun Function.typedExprOrNull(parameter: Type) =
	notNullIf(parameterType == parameter) {
		typed
	}
