package leo13.script

import leo.base.notNullIf
import leo13.Scriptable
import leo13.Type
import leo13.lineTo
import leo13.script

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
