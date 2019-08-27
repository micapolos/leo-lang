package leo13.type

import leo13.script.Scriptable
import leo13.script.asScript
import leo9.Stack
import leo9.mapFirst
import leo9.push
import leo9.stack

data class Functions(val functionStack: Stack<Function>) : Scriptable() {
	override val scriptableName get() = "functions"
	override val scriptableBody get() = functionStack.asScript { scriptableLine }
}

val Stack<Function>.functions get() = Functions(this)
fun Functions.plus(function: Function) = functionStack.push(function).functions
fun functions(vararg functions: Function) = stack(*functions).functions

fun Functions.typedOrNull(parameter: Type) =
	functionStack.mapFirst {
		typedOrNull(parameter)
	}
