package leo32

import leo.base.appendableString
import leo.binary.Array32
import leo.binary.Stack32
import leo.binary.foldIndexed

val Appendable.appendBreak
	get() =
		append(" ")

fun Appendable.appendCode(int: Int) =
	append(int.toString()).append(" ")

fun <T> Appendable.appendCode(indexed: IndexedValue<T>, appendValueFn: Appendable.(T) -> Appendable) =
	this
		.append('[')
		.appendCode(indexed.index)
		.append(']')
		.appendBreak
		.run { appendValueFn(indexed.value) }
		.appendBreak

fun <T> Appendable.appendCode(array: Array32<T>, appendValueFn: Appendable.(T) -> Appendable) =
	foldIndexed(array) { appendCode(it, appendValueFn) }

fun <T> Appendable.appendCode(stack: Stack32<T>, appendValueFn: Appendable.(T) -> Appendable) =
	foldIndexed(stack.array32) {
		appendValueFn(it.value).appendBreak
	}

fun Appendable.appendCode(function: Function) =
	this
		.append("function")
		.appendBreak
		.appendCode(function.matchArray) { appendCode(it) }
		.appendBreak

fun Appendable.appendCode(match: Match) =
	this
		.append("match")
		.appendBreak
		.appendBreak

fun Appendable.appendCode(runtime: Runtime) =
	this
		.append("runtime")
		.appendBreak
		.append("scope")
		.appendBreak
		.appendCode(runtime.scopeFunction)
		.appendBreak
		.append("args")
		.appendBreak
		.appendCode(runtime.argStack) { append(it.toString()) }
		.appendBreak
		.append("functions")
		.appendBreak
		.appendCode(runtime.functionStack) { appendCode(it) }
		.appendBreak
		.appendBreak

fun Appendable.appendCode(leo: Leo) =
	this
		.append("leo")
		.appendBreak
		.appendCode(leo.runtime)
		.appendBreak

val Leo.code
	get() = appendableString { it.appendCode(this) }