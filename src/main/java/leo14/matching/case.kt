package leo14.matching

import leo.base.notNullIf
import leo14.ScriptLine

data class Case<out R>(val name: String, val fn: (ScriptLine) -> R)

infix fun <R> String.caseTo(fn: (ScriptLine) -> R) = Case(this, fn)

fun <R : Any> Case<R>.matchOrNull(scriptLine: ScriptLine): R? =
	notNullIf(scriptLine.name == name) {
		fn(scriptLine)
	}
