package leo13.script

import leo.base.notNullIf
import leo13.AsScriptLine
import leo13.Type
import leo13.lineTo
import leo13.script

data class Function(val parameterType: Type, val typed: Typed) : AsScriptLine() {
	override val asScriptLine = "function" lineTo script(
		"parameter" lineTo script(parameterType.asScriptLine),
		typed.asScriptLine)
}

fun function(parameterType: Type, typed: Typed) = Function(parameterType, typed)

fun Function.typedExprOrNull(parameter: Type) =
	notNullIf(parameterType == parameter) {
		typed
	}
