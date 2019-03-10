package leo32

import leo.binary.*

data class Runtime(
	val scopeFunction: Function,
	val bitStack: Stack32<Bit>,
	val functionStack: Stack32<Function>)

val emptyRuntime =
	Runtime(emptyFunction, emptyStack32(), emptyStack32())

fun Runtime.invoke(bit: Bit): Runtime? =
	(functionStack.top ?: scopeFunction).invoke(bit, this)

fun Runtime.invoke(log: Log) =
	bitStack.updateTop { apply { println("[${log.tag.string}] $this") } }
		?.let { copy(bitStack = it) }

fun Runtime.push(bit: Bit) =
	bitStack.push(bit)?.let { copy(bitStack = it) }

val Runtime.pop
	get() =
		bitStack.shrink?.let { copy(bitStack = it) }

fun Runtime.and(bit: Bit) =
	bitStack.updateTop { and(bit) }?.let { copy(bitStack = it) }

fun Runtime.or(bit: Bit) =
	bitStack.updateTop { or(bit) }?.let { copy(bitStack = it) }

fun Runtime.not(bit: Bit) =
	bitStack.updateTop { or(bit) }?.let { copy(bitStack = it) }

fun Runtime.push(function: Function) =
	functionStack.push(function)?.let { copy(functionStack = it) }
