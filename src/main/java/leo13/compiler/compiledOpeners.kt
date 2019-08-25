package leo13.compiler

import leo13.AsScriptLine
import leo13.asScriptLine
import leo9.Stack
import leo9.push
import leo9.stack

data class CompiledOpeners(
	val stack: Stack<CompiledOpener>) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine
		get() = stack.asScriptLine("openers") { asScriptLine }
}

fun compiledOpeners(stack: Stack<CompiledOpener>) = CompiledOpeners(stack)
fun compiledOpeners(vararg openers: CompiledOpener) = CompiledOpeners(stack(*openers))

fun CompiledOpeners.push(opener: CompiledOpener) = compiledOpeners(stack.push(opener))
