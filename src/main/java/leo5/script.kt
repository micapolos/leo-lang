package leo5

import leo.base.Empty
import leo.base.empty
import leo.base.fold

sealed class Script

data class EmptyScript(val empty: Empty) : Script()
data class ApplicationScript(val application: Application) : Script()
data class FunctionScript(val function: Function) : Script()

fun script(empty: Empty): Script = EmptyScript(empty)
fun script(application: Application): Script = ApplicationScript(application)
fun script(function: Function): Script = FunctionScript(function)

val Script.emptyOrNull get() = (this as? EmptyScript)?.empty
val Script.applicationOrNull get() = (this as? ApplicationScript)?.application
val Script.functionOrNull get() = (this as? FunctionScript)?.function

fun Script.plus(vararg lines: Line) = fold(lines) { script(application(value(this), it)) }
fun script(vararg lines: Line) = script(empty).plus(*lines)

val Script.invokeLhs get() = applicationOrNull!!.value
val Script.invokeRhs get() = applicationOrNull!!.line.value
fun Script.invokeCall(argument: Value) = (lhs as FunctionScript).function.invoke(argument)

val Script.isEmpty get() = emptyOrNull == empty
val Script.simpleLineOrNull get() = applicationOrNull?.simpleLineOrNull
val Script.nameOrNull get() = simpleLineOrNull?.name
fun Script.invoke(argument: Value) = functionOrNull!!.invoke(argument)

fun Appendable.append(script: Script): Appendable = when (script) {
	is EmptyScript -> this
	is ApplicationScript -> append(script.application)
	is FunctionScript -> append('*')
}
