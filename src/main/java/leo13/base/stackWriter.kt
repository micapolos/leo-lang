package leo13.base

import leo13.fail
import leo13.script.Script
import leo9.Stack
import leo9.push

data class StackWriter<V>(val stack: Stack<V>) : Writer<V> {
	override fun write(value: V) = writer(stack.push(value))
	override fun error(script: Script) = fail<Unit>(script)
	override val finish = Unit
}

fun <V> writer(stack: Stack<V>): Writer<V> =
	StackWriter(stack)
