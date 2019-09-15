package leo13

import leo.base.fold
import leo9.Stack
import leo9.fold
import leo9.reverse

interface Processor<V> : Scripting {
	fun process(value: V): Processor<V>
}

fun <V, R> Processor<R>.map(fn: V.() -> R): Processor<V> =
	processor { process(it.fn()) }

fun <V, R> Processor<R>.bind(fn: V.() -> Processor<R>): Processor<V> =
	processor { it.fn() }

fun Processor<Char>.charProcess(string: String): Processor<Char> =
	fold(string) { process(it) }

fun <V> Processor<V>.process(stack: Stack<V>): Processor<V> =
	fold(stack.reverse) { process(it) }
