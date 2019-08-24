package leo13.script

import leo13.Scriptable
import leo13.Type
import leo13.asScriptLine
import leo9.Stack
import leo9.mapFirst
import leo9.push
import leo9.stack

data class Functions(val functionStack: Stack<Function>) : Scriptable() {
	override val asScriptLine = functionStack.asScriptLine("functions") { asScriptLine }
}

val Stack<Function>.functions get() = Functions(this)
fun Functions.plus(function: Function) = functionStack.push(function).functions
fun functions(vararg functions: Function) = stack(*functions).functions

fun Functions.typedExprOrNull(parameter: Type) =
	functionStack.mapFirst {
		typedExprOrNull(parameter)
	}
