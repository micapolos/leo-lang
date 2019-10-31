package leo13.js

import leo.base.fold
import java.math.BigDecimal

typealias Plain = Nothing

sealed class Script<out T>
data class UnitScript<T>(val unit: Unit) : Script<T>()
data class LinkScript<T>(val link: ScriptLink<T>) : Script<T>()

sealed class ScriptLine<T>
data class ValueScriptLine<T>(val value: T) : ScriptLine<T>()
data class StringScriptLine<T>(val string: String) : ScriptLine<T>()
data class BigDecimalScriptLine<T>(val bigDecimal: BigDecimal) : ScriptLine<T>()
data class ScriptLineLine<T>(val field: ScriptField<T>) : ScriptLine<T>()

data class ScriptLink<T>(val lhs: Script<T>, val line: ScriptLine<T>)
data class ScriptField<T>(val string: String, val rhs: Script<T>)

fun <T> script(unit: Unit): Script<T> = UnitScript(unit)
fun <T> line(value: T): ScriptLine<T> = ValueScriptLine(value)
fun <T> line(string: String): ScriptLine<T> = StringScriptLine(string)
fun <T> line(bigDecimal: BigDecimal): ScriptLine<T> = BigDecimalScriptLine(bigDecimal)
fun <T> line(field: ScriptField<T>): ScriptLine<T> = ScriptLineLine(field)
fun <T> line(int: Int): ScriptLine<T> = line(BigDecimal(int))
fun <T> line(double: Double): ScriptLine<T> = line(BigDecimal(double))
fun <T> Script<T>.plus(vararg lines: ScriptLine<T>) = fold(lines) { LinkScript(this linkTo it) }
fun <T> script(vararg lines: ScriptLine<T>): Script<T> = script<T>(Unit).plus(*lines)
fun <T> script(field: ScriptField<T>, vararg fields: ScriptField<T>): Script<T> =
	script(line(field)).fold(fields) { plus(line(it)) }

infix fun <T> String.fieldTo(rhs: Script<T>) = ScriptField(this, rhs)
infix fun <T> String.fieldTo(int: Int) = fieldTo(script(line<T>(int)))
infix fun <T> String.fieldTo(double: Double) = fieldTo(script(line<T>(double)))
infix fun <T> String.fieldTo(string: String) = fieldTo(script(line<T>(string)))
infix fun <T> Script<T>.linkTo(line: ScriptLine<T>) = ScriptLink(this, line)

fun <T> Script<T>.code(fn: T.() -> String): String =
	when (this) {
		is UnitScript -> ""
		is LinkScript -> link.code(fn)
	}

fun <T> ScriptLine<T>.code(fn: T.() -> String): String =
	when (this) {
		is ValueScriptLine -> value.fn()
		is StringScriptLine -> "\"$string\""
		is BigDecimalScriptLine -> "$bigDecimal"
		is ScriptLineLine -> field.code(fn)
	}

fun <T> ScriptLink<T>.code(fn: T.() -> String) =
	if (lhs is UnitScript) "${line.code(fn)}"
	else "${lhs.code(fn)}.${line.code(fn)}"

fun <T> ScriptField<T>.code(fn: T.() -> String) =
	"$string(${rhs.code(fn)})"
