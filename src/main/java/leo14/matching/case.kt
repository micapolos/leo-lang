package leo14.matching

import leo.base.notNullIf
import leo14.Script
import leo14.ScriptLine
import leo14.script

data class Case<out R>(val name: String, val fn: (Script) -> R)

infix fun <R> String.caseTo(fn: (Script) -> R) = Case(this, fn)

fun <R : Any> Case<R>.matchOrNull(scriptLine: ScriptLine): R? =
	notNullIf(scriptLine.name == name) {
		fn(script(scriptLine))
	}
