package leo13.base

import leo13.script.Script
import leo9.stack

interface Writer<in V> {
	fun write(value: V): Writer<V>
	fun error(script: Script)
	val finish: Unit
}

fun <V> writer(vararg values: V): Writer<V> =
	StackWriter(stack(values))
