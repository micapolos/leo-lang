package leo13.compiler

import leo13.*
import leo13.script.asScript
import leo13.type.Type

data class Functions(val functionStack: Stack<Function>) : LeoObject() {
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
