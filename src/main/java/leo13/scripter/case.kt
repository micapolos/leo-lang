package leo13.scripter

import leo13.script.Script
import leo13.script.ScriptLine

data class Case<V : Any, A : Any>(val scripter: Scripter<A>, val valueFn: A.() -> V)

fun <V : Any, A : Any> case(scripter: Scripter<A>, valueFn: A.() -> V) = Case(scripter, valueFn)

fun <V : Any, A : Any> Case<V, A>.unsafeValueOrNull(scriptLine: ScriptLine): V? =
	if (scripter.name == scriptLine.name) scripter.unsafeBodyValue(scriptLine.rhs).valueFn()
	else null

fun <V : Any, A : Any> Case<V, A>.unsafeValue(script: Script) = scripter.unsafeBodyValue(script).valueFn()

