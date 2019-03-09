package leo32

import leo.binary.*

data class Runtime(
	val scopeFunction: Function,
	val argStack: Stack32<Int>,
	val functionStack: Stack32<Function>)

val emptyRuntime =
	Runtime(emptyFunction, emptyStack32(), emptyStack32())

fun Runtime.push(arg: Int): Runtime? =
	(functionStack.top ?: scopeFunction).invoke(arg, this)

fun Runtime.invokeLog(tag: String) =
	argStack.updateTop { apply { println("[$tag] $this") } }
		?.let { copy(argStack = it) }

fun Runtime.invokePush(arg: Int) =
	argStack.push(arg)?.let { copy(argStack = it) }

val Runtime.invokePop
	get() =
		argStack.shrink?.let { copy(argStack = it) }

fun Runtime.invokePlus(arg: Int) =
	argStack.updateTop { plus(arg) }?.let { copy(argStack = it) }

fun Runtime.push(function: Function) =
	functionStack.push(function)?.let { copy(functionStack = it) }