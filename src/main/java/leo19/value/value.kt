package leo19.value

import leo13.Stack
import leo13.get
import leo13.push
import leo19.type.Type

data class Scope(val stack: Stack<Value>)

sealed class Value
data class NativeValue(val any: Any?) : Value()
data class IntValue(val int: Int) : Value()
data class ListValue(val list: List<Value>) : Value()
data class ListGetValue(val list: Value, val index: Int) : Value()
data class IndexedValue(val index: Int, val value: Value) : Value()
data class ArgumentValue(val index: Int) : Value()
data class FunctionValue(val param: Type, val scope: Scope, val value: Value) : Value()
data class InvokeValue(val function: Value, val param: Value) : Value()

fun nativeValue(any: Any?): Value = NativeValue(any)
fun value(int: Int): Value = IntValue(int)
fun value(vararg values: Value): Value = ListValue(values.toList())
infix fun Int.indexed(value: Value): Value = IndexedValue(this, value)

fun Scope.push(value: Value) = Scope(stack.push(value))

fun Scope.eval(value: Value): Value =
	when (value) {
		is NativeValue -> value
		is IntValue -> value
		is ListValue -> ListValue(value.list.map { eval(it) })
		is ListGetValue -> (eval(value.list) as ListValue).list[value.index]
		is IndexedValue -> IndexedValue(value.index, eval(value.value))
		is ArgumentValue -> stack.get(value.index)!!
		is FunctionValue -> value
		is InvokeValue -> (eval(value.function) as FunctionValue).let { function ->
			function.scope.push(eval(value.param)).eval(function.value)
		}
	}
