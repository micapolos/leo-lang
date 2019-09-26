package leo13.compiler

import leo.base.fold
import leo13.ObjectScripting
import leo13.expression.*
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.script
import leo13.type.Type
import leo13.type.lineTo
import leo13.type.type
import leo13.typedName
import leo13.value.value

data class ExpressionTyped(val expression: Expression, val type: Type) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine
		get() =
			typedName lineTo script(expression.scriptLine, type.scriptingLine)
}

fun typed(expression: Expression, type: Type) =
	ExpressionTyped(expression, type)

fun expressionTyped(vararg lines: ExpressionTypedLine) =
	ExpressionTyped(expression(), type()).fold(lines) { plus(it) }

fun ExpressionTyped.plus(line: ExpressionTypedLine) =
	typed(
		expression.plus(line.op),
		type.plus(line.typeLine))

fun ExpressionTyped.plus(name: String) =
	typed(
		expression.plus(wrap(name).op),
		type(name lineTo type))

val ExpressionTyped.previousOrNull: ExpressionTyped?
	get() =
		type
			.previousOrNull
			?.run { typed(expression.plus(previous.op), this) }

val ExpressionTyped.contentOrNull: ExpressionTyped?
	get() =
		type
			.contentOrNull
			?.run { typed(expression.plus(content.op), this) }

fun ExpressionTyped.getOrNull(name: String): ExpressionTyped? =
	type
		.getOrNull(name)
		?.run {
			typed(
				expression.plus(get(name).op),
				this)
		}

fun ExpressionTyped.plusIn(rhs: ExpressionTyped): ExpressionTyped =
	typed(
		expression.plus(bind(rhs.expression).op),
		rhs.type)

fun ExpressionTyped.plus(typed: SwitchTyped) =
	typed(
		expression.plus(typed.switch.op),
		typed.type)

fun expressionTyped(script: Script): ExpressionTyped =
	typed(expression(script.value.op), script.type)

val ExpressionTyped.isEmpty
	get() =
		type.isEmpty