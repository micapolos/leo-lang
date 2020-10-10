package leo19.term.eval

import leo19.term.Term

sealed class Value
data class IntValue(val int: Int) : Value()
data class TupleValue(val list: List<Value>) : Value()
data class FunctionValue(val scope: Scope, val body: Term) : Value()

fun value(int: Int): Value = IntValue(int)
fun value(vararg values: Value): Value = TupleValue(values.toList())
fun functionValue(scope: Scope, body: Term): Value = FunctionValue(scope, body)
