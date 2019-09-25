package leo13.compiler

import leo.base.notNullIf
import leo13.*
import leo13.expression.apply
import leo13.expression.expression
import leo13.expression.op
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.value.item
import leo13.value.value

data class Functions(val typedFunctionStack: Stack<FunctionTyped>) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = functionsName lineTo typedFunctionStack.scripting.script.emptyIfEmpty
}

fun functions() = Functions(stack())
fun Functions.plus(typedFunction: FunctionTyped) = Functions(typedFunctionStack.push(typedFunction))

fun Functions.resolve(expressionTyped: ExpressionTyped): ExpressionTyped =
	typedFunctionStack
		.mapFirst {
			notNullIf(arrow.lhs.contains(expressionTyped.type)) {
				typed(
					expression(op(value(item(function))), op(apply(expressionTyped.expression))),
					arrow.rhs)
			}
		}
		?: expressionTyped