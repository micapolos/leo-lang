package leo5

sealed class Value

data class ScriptValue(val script: Script) : Value()
data class FunctionValue(val function: Function) : Value()

fun value(script: Script): Value = ScriptValue(script)
fun value(function: Function): Value = FunctionValue(function)

fun Value.apply(line: ValueLine) = value(script(application(this, line)))
fun value(vararg lines: ValueLine) = value(script(*lines))

val Value.isEmpty get() = this is ScriptValue && script is EmptyScript
val Value.script get() = (this as ScriptValue).script
val Value.function get() = (this as FunctionValue).function

val Value.lhs get() = script.lhs
val Value.name get() = script.name
val Value.rhs get() = script.rhs
fun Value.call(value: Value) = invoke(parameter(value))
fun Value.dispatch(vararg pairs: Pair<String, () -> Value>) =
	mapOf(*pairs).getValue(name).invoke()

val Value.int get() = script.application.onlyLine.value("int").script.application.onlyLine.onlyName.toInt()
fun value(int: Int) = value(valueLine(int))

fun Value.invoke(parameter: ValueParameter) = function.body.invoke(parameter)
fun Value.invokeDispatch(bodyDictionary: BodyDictionary, parameter: ValueParameter) =
	bodyDictionary.at(name).invoke(parameter)

fun Appendable.append(value: Value): Appendable = when (value) {
	is ScriptValue -> append(value.script)
	is FunctionValue -> append('*')
}
