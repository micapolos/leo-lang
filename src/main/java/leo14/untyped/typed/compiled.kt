package leo14.untyped.typed

import leo.base.notNullIf
import leo14.*
import leo14.Number
import leo14.untyped.minusName
import leo14.untyped.plusName
import leo14.untyped.timesName

data class Compiled(val type: Type, val expression: Expression)

fun compiled(type: Type, expression: Expression) = Compiled(type, expression)
fun compiled(type: Type, valueFn: ValueFn) = Compiled(type, Expression(valueFn))

fun Expression.textApply(begin: Begin, compiled: Compiled): Compiled? =
	when (begin.string) {
		plusName -> compiled.stringExpressionOrNull?.let { rhs ->
			compiled(textType, this.stringPlusString(rhs))
		}
		else -> null
	}

fun Expression.numberApply(begin: Begin, compiled: Compiled): Compiled? =
	when (begin.string) {
		plusName -> applyNumberOp2(compiled, Number::plus)
		minusName -> applyNumberOp2(compiled, Number::minus)
		timesName -> applyNumberOp2(compiled, Number::times)
		else -> null
	}

fun Expression.applyNumberOp2(compiled: Compiled, fn: Number.(Number) -> Number): Compiled? =
	notNullIf(compiled.type == numberType) {
		compiled(numberType) {
			number.fn(compiled.expression.number)
		}
	}

val Compiled.stringExpressionOrNull: Expression? get() = notNullIf(type == textType) { expression }
val Compiled.numberExpressionOrNull: Expression? get() = notNullIf(type == numberType) { expression }
