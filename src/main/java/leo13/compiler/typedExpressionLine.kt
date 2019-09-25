package leo13.compiler

import leo13.ObjectScripting
import leo13.expression.lineTo
import leo13.expression.op
import leo13.expression.plus
import leo13.lineName
import leo13.type.lineTo
import leo13.script.lineTo
import leo13.script.script

data class TypedExpressionLine(val name: String, val rhs: TypedExpression) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = lineName lineTo script(name lineTo script(rhs.scriptingLine))
}

infix fun String.lineTo(rhs: TypedExpression) = TypedExpressionLine(this, rhs)

val TypedExpressionLine.op get() = plus(name lineTo rhs.expression).op
val TypedExpressionLine.expressionLine get() = name lineTo rhs.expression
val TypedExpressionLine.typeLine get() = name lineTo rhs.type
