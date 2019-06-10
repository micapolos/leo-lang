package leo5

sealed class Value

data class ScriptValue(val script: Script) : Value()
data class FunctionValue(val function: Function) : Value()

fun value() = value(script())
fun value(script: Script): Value = ScriptValue(script)
fun value(function: Function): Value = FunctionValue(function)

val Value.isEmpty get() = (this is ScriptValue) && script is EmptyScript
val Value.scriptOrNull get() = (this as? ScriptValue)?.script
val Value.functionOrNull get() = (this as? FunctionValue)?.function

val Value.invokeLhs get() = scriptOrNull!!.applicationOrNull!!.value
val Value.invokeRhs get() = scriptOrNull!!.applicationOrNull!!.line.value
fun Value.invoke(parameter: ValueParameter) = functionOrNull!!.body.invoke(parameter)
fun Value.invokeDispatch(bodyDictionary: BodyDictionary, parameter: ValueParameter) =
	bodyDictionary.at(scriptOrNull!!.applicationOrNull!!.line.name).invoke(parameter)

fun Appendable.append(value: Value): Appendable = when (value) {
	is ScriptValue -> append(value.script)
	is FunctionValue -> append('*')
}
