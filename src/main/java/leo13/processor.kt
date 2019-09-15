package leo13

import leo.base.fold
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo9.*

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

fun <V : Scripting> processorStack(fn: Processor<V>.() -> Processor<V>): Stack<V> =
	(CapturingProcessor<V>(stack()).fn() as CapturingProcessor).stack

data class CapturingProcessor<V : Scripting>(val stack: Stack<V>) : ObjectScripting(), Processor<V> {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = "processor" lineTo script(
			"captured" lineTo stack.scripting.script)

	override fun process(value: V): Processor<V> =
		CapturingProcessor(stack.push(value))
}
