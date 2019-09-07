package leo13.base.type

import leo13.fail
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.onlyLineOrNull
import leo9.Stack
import leo9.mapFirst
import leo9.stack

data class Choice<V : Any>(
	val caseStack: Stack<Case<V, *>>,
	val caseFn: V.() -> Pair<Type<*>, Any?>)

fun <V : Any, V1 : Any, V2 : Any> choice(case1: Case<V, V1>, case2: Case<V, V2>, fn: V.() -> Pair<Type<*>, Any?>): Choice<V> =
	Choice(stack(case1, case2), fn)

fun <V : Any> Choice<V>.script(value: V): Script =
	value.caseFn().let { pair ->
		leo13.script.script((pair.first as Type<Any>).scriptLine(pair.second as Any))
	}

fun <V : Any> Choice<V>.unsafeValue(script: Script): V =
	script.onlyLineOrNull?.let { line ->
		caseStack.mapFirst { unsafeValueOrNull(line) }
	} ?: fail("mismatch" lineTo script)
