package leo13.base

import leo13.LeoObject
import leo13.Scriptable
import leo13.script.asScript
import leo9.Stack
import leo9.push

data class StackWriter<V : Scriptable>(val stack: Stack<V>) : LeoObject(), Writer<V> {
	override fun toString() = super.toString()
	override val scriptableName get() = "writer"
	override val scriptableBody get() = stack.asScript { scriptableLine }

	override fun write(value: V) = writer(stack.push(value))
	override val finishWriting = Unit
}

fun <V : Scriptable> writer(stack: Stack<V>): Writer<V> =
	StackWriter(stack)
