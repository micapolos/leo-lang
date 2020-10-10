package leo19.term.eval

import leo19.term.Term

sealed class Value
data class IntValue(val int: Int) : Value()
data class TupleValue(val list: List<Value>) : Value()
data class FunctionValue(val function: Function) : Value()
data class Function(val scope: Scope, val body: Term)

fun value(int: Int): Value = IntValue(int)
fun value(vararg values: Value): Value = TupleValue(values.toList())
fun value(function: Function): Value = FunctionValue(function)
fun function(scope: Scope, body: Term) = Function(scope, body)

