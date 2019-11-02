package leo13.js.compiler

sealed class Expression

object NullExpression : Expression()
data class NumberExpression(val number: Number) : Expression()
data class StringExpression(val string: String) : Expression()
data class NativeExpression(val native: Native) : Expression()
data class LinkExpression(val link: ExpressionLink) : Expression()
data class ApplyExpression(val apply: Apply) : Expression()
data class BindExpression(val bind: Bind) : Expression()
data class BoundExpression(val bound: Bound) : Expression()
data class CallExpression(val call: Call) : Expression()
data class GetExpression(val get: Get) : Expression()
data class SetExpression(val set: Set) : Expression()
data class InvokeExpression(val invoke: Invoke) : Expression()
data class ArgumentExpression(val argument: Argument) : Expression()
data class LambdaExpression(val lambda: Lambda) : Expression()

val nullExpression: Expression = NullExpression
fun expression(number: Number): Expression = NumberExpression(number)
fun expression(string: String): Expression = StringExpression(string)
fun expression(native: Native): Expression = NativeExpression(native)
fun expression(link: ExpressionLink): Expression = LinkExpression(link)
fun expression(pipe: Apply): Expression = ApplyExpression(pipe)
fun expression(push: Bind): Expression = BindExpression(push)
fun expression(bound: Bound): Expression = BoundExpression(bound)
fun expression(call: Call): Expression = CallExpression(call)
fun expression(get: Get): Expression = GetExpression(get)
fun expression(set: Set): Expression = SetExpression(set)
fun expression(invoke: Invoke): Expression = InvokeExpression(invoke)
fun expression(argument: Argument): Expression = ArgumentExpression(argument)
fun expression(lambda: Lambda): Expression = LambdaExpression(lambda)

val Expression.code: String
	get() =
		when (this) {
			is NullExpression -> "null"
			is NumberExpression -> number.code
			is StringExpression -> string.code
			is NativeExpression -> native.code
			is LinkExpression -> link.code
			is ApplyExpression -> apply.code
			is BindExpression -> bind.code
			is BoundExpression -> bound.code
			is CallExpression -> call.code
			is GetExpression -> get.code
			is SetExpression -> set.code
			is InvokeExpression -> invoke.code
			is ArgumentExpression -> argument.code
			is LambdaExpression -> lambda.code
		}

val Expression.returnCode
	get() =
		if (this is LinkExpression) "${link.lhs.code}; return ${link.rhs.code}"
		else "return $code"

infix fun Expression.then(rhs: Expression) =
	if (this is NullExpression) rhs
	else expression(this linkTo rhs)
