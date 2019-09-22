package leo13

import leo.base.Seq
import leo.base.fold
import leo13.script.ScriptLine
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.script.script

interface Processor<V> : Scripting {
	fun process(value: V): Processor<V>
}

fun <V, R> Processor<R>.map(fn: V.() -> R): Processor<V> =
	processor { process(it.fn()) }

fun <V, R> Processor<R>.bind(fn: V.() -> Processor<R>): Processor<V> =
	processor { it.fn() }

fun Processor<Char>.charProcess(string: String): Processor<Char> =
	fold(string) { process(it) }

fun <V> Processor<V>.processAll(stack: Stack<V>): Processor<V> =
	fold(stack.reverse) { process(it) }

fun <V> Processor<V>.process(seq: Seq<V>): Processor<V> =
	fold(seq) { process(it) }

fun <A : Scripting> processorStack(fn: Processor<A>.() -> Unit): Stack<A> {
	val capturingProcessor = CapturingProcessor<A>(stack())
	capturingProcessor.fn()
	return capturingProcessor.capturedStack
}

data class CapturingProcessor<V : Scripting>(var capturedStack: Stack<V>) : ObjectScripting(), Processor<V> {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = "processor" lineTo script(
			"captured" lineTo capturedStack.scripting.script.emptyIfEmpty)

	override fun process(value: V): Processor<V> {
		capturedStack = capturedStack.push(value)
		return this
	}
}
