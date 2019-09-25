package leo13.compiler

import leo.base.fold
import leo13.ObjectScripting
import leo13.expression.*
import leo13.isEmpty
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.script
import leo13.type.Type
import leo13.type.lineTo
import leo13.type.type
import leo13.typedName
import leo13.value.value

data class TypedExpression(val expression: Expression, val type: Type) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine
		get() =
			typedName lineTo script(expression.scriptLine, type.scriptingLine)
}

fun typed(expression: Expression, type: Type) =
	TypedExpression(expression, type)

fun typed(vararg lines: TypedExpressionLine) =
	TypedExpression(expression(), type()).fold(lines) { plus(it) }

fun TypedExpression.plus(line: TypedExpressionLine) =
	typed(
		expression.plus(line.op),
		type.plus(line.typeLine))

fun TypedExpression.plus(name: String) =
	typed(
		expression.plus(wrap(name).op),
		type(name lineTo type))

val TypedExpression.previousOrNull: TypedExpression?
	get() =
		type
			.previousOrNull
			?.run { typed(expression.plus(previous.op), this) }

val TypedExpression.contentOrNull: TypedExpression?
	get() =
		type
			.contentOrNull
			?.run { typed(expression.plus(content.op), this) }

fun TypedExpression.getOrNull(name: String): TypedExpression? =
	type
		.getOrNull(name)
		?.run {
			typed(
				expression.plus(get(name).op),
				this)
		}

fun TypedExpression.plusIn(rhs: TypedExpression): TypedExpression =
	typed(
		expression.plus(bind(rhs.expression).op),
		rhs.type)

fun TypedExpression.plus(typed: TypedSwitch) =
	typed(
		expression.plus(typed.switch.op),
		typed.type)

fun typed(script: Script): TypedExpression =
	typed(expression(script.value.op), script.type)

val TypedExpression.isEmpty
	get() =
		expression.opStack.isEmpty && type.isEmpty