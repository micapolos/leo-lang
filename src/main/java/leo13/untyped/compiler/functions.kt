package leo13.untyped.compiler

import leo13.ObjectScripting
import leo13.script
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.scripting
import leo9.Stack
import leo9.push
import leo9.stack

data class Functions(val functionStack: Stack<FunctionCompiled>) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = "functions" lineTo functionStack.scripting.script.emptyIfEmpty
}

fun functions() = Functions(stack())
fun Functions.plus(function: FunctionCompiled) = Functions(functionStack.push(function))