package leo13.untyped.evaluator

import leo13.untyped.expression.Expression
import leo13.untyped.functionName

data class Function(val expression: Expression) {
	override fun toString() = functionName
}
