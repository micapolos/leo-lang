package leo19.value

import leo19.expr.Expr

sealed class Value
object NullValue : Value()
data class IntValue(val int: Int) : Value()
data class PairValue(val lhs: Value, val rhs: Value) : Value()
data class LhsValue(val pair: Value) : Value()
data class RhsValue(val pair: Value) : Value()
data class ArrayValue(val list: List<Value>) : Value()
data class FunctionValue(val function: Function) : Value()
data class Function(val scope: Scope, val body: Expr, val isRecursive: Boolean)

val nullValue: Value = NullValue
fun value(int: Int): Value = IntValue(int)
fun value(vararg values: Value): Value = ArrayValue(values.toList())
fun value(function: Function): Value = FunctionValue(function)
fun function(scope: Scope, body: Expr, isRecursive: Boolean) = Function(scope, body, isRecursive = isRecursive)
fun function(scope: Scope, body: Expr) = function(scope, body, isRecursive = false)
fun recursiveFunction(scope: Scope, body: Expr) = function(scope, body, isRecursive = true)
fun Value.to(rhs: Value): Value = PairValue(this, rhs)
val Value.lhs get(): Value = LhsValue(this)
val Value.rhs get(): Value = RhsValue(this)
