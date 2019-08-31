package leo13.compiler

import leo13.script.Scriptable
import leo13.script.asScript
import leo13.type.Type
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

fun Functions.compiledOrNull(parameter: Type): Compiled? =
	functionStack.mapFirst {
		compiledOrNull(parameter)
	}
