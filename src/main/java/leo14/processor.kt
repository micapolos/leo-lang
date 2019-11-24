package leo14

import leo13.Stack
import leo13.push
import leo13.stack

data class Processor<T>(val processFn: (T) -> Processor<T>)

fun <T> processor(process: (T) -> Processor<T>) = Processor(process)

fun <T> Processor<T>.process(value: T) = processFn(value)

fun <T, R> Processor<T>.map(fn: (R) -> T): Processor<R> =
	processor { processFn(fn(it)).map(fn) }

fun <T> printlnProcessor(): Processor<T> =
	processor { println(it); printlnProcessor() }

fun <T> consumeProcessor(consume: (T) -> Unit): Processor<T> =
	processor { consume(it); consumeProcessor(consume) }

fun <T> processStack(fn: Processor<T>.() -> Unit): Stack<T> {
	var stack = stack<T>()
	consumeProcessor<T> { stack = stack.push(it) }.fn()
	return stack
}

fun <R, T> R.foldProcessor(step: R.(T) -> R, fn: Processor<T>.() -> Unit): R {
	var folded = this
	consumeProcessor<T> { folded = folded.step(it) }.fn()
	return folded
}

fun processorString(fn: Processor<String>.() -> Unit): String =
	StringBuilder()
		.foldProcessor<StringBuilder, String>({ append(it) }, fn)
		.toString()