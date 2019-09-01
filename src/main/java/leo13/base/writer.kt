package leo13.base

import leo13.Scriptable
import leo13.script.Script
import leo9.stack

interface Writer<in V> : Scriptable {
	fun write(value: V): Writer<V>
	fun error(script: Script)
	val finish: Unit
}

fun <V : Scriptable> writer(vararg values: V): Writer<V> =
	StackWriter(stack(*values))
