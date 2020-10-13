package leo19.value

import leo19.expr.Expr
import leo19.expr.eval.Scope

sealed class Value
object NullValue : Value()
data class IntValue(val int: Int) : Value()
data class ArrayValue(val list: List<Value>) : Value()
data class FunctionValue(val function: Function) : Value()
data class Function(val scope: Scope, val body: Expr)

val nullValue: Value = NullValue
fun value(int: Int): Value = IntValue(int)
fun value(vararg values: Value): Value = ArrayValue(values.toList())
fun value(function: Function): Value = FunctionValue(function)
fun function(scope: Scope, body: Expr) = Function(scope, body)

