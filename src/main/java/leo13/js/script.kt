package leo13.js

import leo.base.fold
import java.math.BigDecimal

typealias Plain = Nothing

sealed class Script<out T>
data class UnitScript<T>(val unit: Unit) : Script<T>()
data class LinkScript<T>(val link: ScriptLink<T>) : Script<T>()

sealed class ScriptItem<T>
data class ValueScriptItem<T>(val value: T) : ScriptItem<T>()
data class StringScriptItem<T>(val string: String) : ScriptItem<T>()
data class BigDecimalScriptItem<T>(val bigDecimal: BigDecimal) : ScriptItem<T>()
data class ScriptLineItem<T>(val line: ScriptLine<T>) : ScriptItem<T>()

data class ScriptLink<T>(val lhs: Script<T>, val item: ScriptItem<T>)
data class ScriptLine<T>(val string: String, val rhs: Script<T>)

fun <T> script(unit: Unit): Script<T> = UnitScript(unit)
fun <T> item(value: T): ScriptItem<T> = ValueScriptItem(value)
fun <T> item(string: String): ScriptItem<T> = StringScriptItem(string)
fun <T> item(bigDecimal: BigDecimal): ScriptItem<T> = BigDecimalScriptItem(bigDecimal)
fun <T> item(line: ScriptLine<T>): ScriptItem<T> = ScriptLineItem(line)
fun <T> item(int: Int): ScriptItem<T> = item(BigDecimal(int))
fun <T> item(double: Double): ScriptItem<T> = item(BigDecimal(double))
fun <T> Script<T>.plus(vararg items: ScriptItem<T>) = fold(items) { LinkScript(this linkTo it) }
fun <T> script(vararg items: ScriptItem<T>): Script<T> = script<T>(Unit).plus(*items)

infix fun <T> String.lineTo(rhs: Script<T>) = ScriptLine(this, rhs)
infix fun <T> Script<T>.linkTo(item: ScriptItem<T>) = ScriptLink(this, item)

fun <T> Script<T>.code(fn: T.() -> String): String =
	when (this) {
		is UnitScript -> ""
		is LinkScript -> link.code(fn)
	}

fun <T> ScriptItem<T>.code(fn: T.() -> String): String =
	when (this) {
		is ValueScriptItem -> value.fn()
		is StringScriptItem -> "\"$string\""
		is BigDecimalScriptItem -> "$bigDecimal"
		is ScriptLineItem -> line.code(fn)
	}

fun <T> ScriptLink<T>.code(fn: T.() -> String) =
	if (lhs is UnitScript) "${item.code(fn)}"
	else "${lhs.code(fn)}.${item.code(fn)}"

fun <T> ScriptLine<T>.code(fn: T.() -> String) =
	"$string(${rhs.code(fn)})"
