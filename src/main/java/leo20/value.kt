package leo20

import leo.base.fold
import leo.base.reverse
import leo13.Stack
import leo13.first
import leo13.onlyOrNull
import leo13.push
import leo13.stack
import leo14.FieldScriptLine
import leo14.Literal
import leo14.LiteralScriptLine
import leo14.NumberLiteral
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.StringLiteral
import leo14.bigDecimal
import leo14.lineSeq
import java.math.BigDecimal

data class Value(val lineStack: Stack<Line>)

sealed class Line
data class FieldLine(val field: Field) : Line()
data class StringLine(val string: String) : Line()
data class NumberLine(val number: Number) : Line()
data class FunctionLine(val function: Function) : Line()

data class Field(val name: String, val rhs: Value)
data class Function(val scope: Scope, val body: Script)

val emptyValue = Value(stack())
fun Value.plus(line: Line) = Value(lineStack.push(line))
fun value(vararg lines: Line) = emptyValue.fold(lines) { plus(it) }
infix fun String.lineTo(rhs: Value): Line = FieldLine(Field(this, rhs))
fun line(function: Function): Line = FunctionLine(function)
fun line(string: String): Line = StringLine(string)
fun line(int: Int): Line = NumberLine(int.bigDecimal)
fun line(bigDecimal: BigDecimal): Line = NumberLine(bigDecimal)
fun Scope.function(body: Script) = Function(this, body)

val Line.selectName: String
	get() =
		when (this) {
			is FieldLine -> field.name
			is StringLine -> "text"
			is NumberLine -> "number"
			is FunctionLine -> "function"
		}

val Line.fieldOrNull get() = (this as? FieldLine)?.field
val Line.functionOrNull get() = (this as? FunctionLine)?.function

val Value.bodyOrNull: Value?
	get() =
		lineStack.onlyOrNull?.fieldOrNull?.rhs

fun Value.lineOrNull(name: String): Line? =
	lineStack.first { it.selectName == name }

fun Value.getOrNull(name: String): Value? =
	bodyOrNull?.lineOrNull(name)?.let { value(it) }

fun Value.make(name: String): Value =
	value(name lineTo this)

fun Value.applyOrNull(param: Value): Value? =
	lineStack.onlyOrNull?.functionOrNull?.apply(param)

fun Value.apply(param: Value): Value =
	applyOrNull(param) ?: plus("apply" lineTo param)

fun Function.apply(param: Value): Value =
	scope.push(param).value(body)
