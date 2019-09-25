package leo13.compiler

import leo13.ObjectScripting
import leo13.expression.lineTo
import leo13.expression.op
import leo13.expression.plus
import leo13.lineName
import leo13.script.lineTo
import leo13.script.script
import leo13.type.lineTo

data class ExpressionTypedLine(val name: String, val rhs: ExpressionTyped) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = lineName lineTo script(name lineTo script(rhs.scriptingLine))
}

infix fun String.lineTo(rhs: ExpressionTyped) = ExpressionTypedLine(this, rhs)

val ExpressionTypedLine.op get() = plus(name lineTo rhs.expression).op
val ExpressionTypedLine.expressionLine get() = name lineTo rhs.expression
val ExpressionTypedLine.typeLine get() = name lineTo rhs.type
