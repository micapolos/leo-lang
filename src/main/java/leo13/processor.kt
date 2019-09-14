package leo13

import leo.base.fold

interface Processor<V> : Scripting {
	fun process(value: V): Processor<V>
}

fun <V, R> Processor<R>.map(fn: V.() -> R): Processor<V> =
	processor { process(it.fn()) }

fun <V, R> Processor<R>.bind(fn: V.() -> Processor<R>): Processor<V> =
	processor { it.fn() }

fun Processor<Char>.charProcess(string: String): Processor<Char> =
	fold(string) { process(it) }