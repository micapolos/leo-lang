package leo13.compiler

import leo.base.fold
import leo13.ObjectScripting
import leo13.compiledName
import leo13.expression.*
import leo13.isEmpty
import leo13.type.Type
import leo13.type.lineTo
import leo13.type.type
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.script
import leo13.value.value

data class Compiled(val expression: Expression, val type: Type) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine
		get() =
			compiledName lineTo script(expression.scriptLine, type.scriptingLine)
}

fun compiled(expression: Expression, type: Type) =
	Compiled(expression, type)

fun compiled(vararg lines: CompiledLine) =
	Compiled(expression(), type()).fold(lines) { plus(it) }

fun Compiled.plus(line: CompiledLine) =
	compiled(
		expression.plus(line.op),
		type.plus(line.typeLine))

fun Compiled.plus(name: String) =
	compiled(
		expression.plus(wrap(name).op),
		type(name lineTo type))

val Compiled.previousOrNull: Compiled?
	get() =
		type
			.previousOrNull
			?.run { compiled(expression.plus(previous.op), this) }

val Compiled.contentOrNull: Compiled?
	get() =
		type
			.contentOrNull
			?.run { compiled(expression.plus(content.op), this) }

fun Compiled.getOrNull(name: String): Compiled? =
	type
		.getOrNull(name)
		?.run {
			compiled(
				expression.plus(get(name).op),
				this)
		}

fun Compiled.plusIn(rhs: Compiled): Compiled =
	compiled(
		expression.plus(bind(rhs.expression).op),
		rhs.type)

fun Compiled.plus(compiled: SwitchCompiled) =
	compiled(
		expression.plus(compiled.switch.op),
		compiled.type)

fun compiled(script: Script): Compiled =
	compiled(expression(script.value.op), script.type)

val Compiled.isEmpty
	get() =
		expression.opStack.isEmpty && type.isEmpty