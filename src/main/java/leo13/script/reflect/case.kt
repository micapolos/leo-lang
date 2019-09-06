package leo13.script.reflect

import leo13.script.Script
import leo13.script.ScriptLine

data class Case<V : Any, A : Any>(val type: Type<A>, val valueFn: A.() -> V)

fun <V : Any, A : Any> case(type: Type<A>, valueFn: A.() -> V) = Case(type, valueFn)

fun <V : Any, A : Any> Case<V, A>.unsafeValueOrNull(scriptLine: ScriptLine): V? =
	if (type.name == scriptLine.name) type.unsafeBodyValue(scriptLine.rhs).valueFn()
	else null

fun <V : Any, A : Any> Case<V, A>.unsafeValue(script: Script) = type.unsafeBodyValue(script).valueFn()

