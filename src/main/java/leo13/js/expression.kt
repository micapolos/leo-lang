package leo13.js

sealed class Expression

object NullExpression : Expression()
data class DoubleExpression(val double: Double) : Expression()
data class StringExpression(val string: String) : Expression()
data class ApplyExpression(val apply: Apply) : Expression()
data class BindExpression(val bind: Bind) : Expression()
data class BoundExpression(val bound: Bound) : Expression()
data class CallExpression(val call: Call) : Expression()
data class GetExpression(val get: Get) : Expression()
data class SetExpression(val set: Set) : Expression()

val nullExpression: Expression = NullExpression
fun expression(double: Double): Expression = DoubleExpression(double)
fun expression(string: String): Expression = StringExpression(string)
fun expression(pipe: Apply): Expression = ApplyExpression(pipe)
fun expression(push: Bind): Expression = BindExpression(push)
fun expression(bound: Bound): Expression = BoundExpression(bound)
fun expression(call: Call): Expression = CallExpression(call)
fun expression(get: Get): Expression = GetExpression(get)
fun expression(set: Set): Expression = SetExpression(set)

val Expression.code: String
	get() =
		when (this) {
			is NullExpression -> "null"
			is DoubleExpression -> "($double)"
			is StringExpression -> "\'$string\'"
			is ApplyExpression -> apply.code
			is BindExpression -> bind.code
			is BoundExpression -> bound.code
			is CallExpression -> call.code
			is GetExpression -> get.code
			is SetExpression -> set.code
		}