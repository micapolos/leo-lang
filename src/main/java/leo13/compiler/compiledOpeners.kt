package leo13.compiler

import leo13.Scriptable
import leo13.asScript
import leo9.Stack
import leo9.push
import leo9.stack

data class CompiledOpeners(
	val stack: Stack<CompiledOpener>) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "openers"
	override val scriptableBody get() = stack.asScript { scriptableLine }
}

fun compiledOpeners(stack: Stack<CompiledOpener>) = CompiledOpeners(stack)
fun compiledOpeners(vararg openers: CompiledOpener) = CompiledOpeners(stack(*openers))

fun CompiledOpeners.push(opener: CompiledOpener) = compiledOpeners(stack.push(opener))
