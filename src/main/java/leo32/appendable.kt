@file:Suppress("UNUSED_PARAMETER")

package leo32

import leo.binary.Array1
import leo.binary.Stack32
import leo.binary.foldIndexed
import leo.binary.int

val Appendable.appendBeginCode
	get() =
		append(' ')

val Appendable.appendEndCode
	get() =
		append('.')

fun Appendable.appendCode(string: String) =
	appendScriptCode {
		append('"').append(string).append('"')
	}

fun Appendable.appendWordCode(wordString: String) =
	append(wordString).appendBeginCode

fun Appendable.appendCode(int: Int) =
	append(int.toString())

fun Appendable.appendIndexCode(int: Int) =
	appendCode(int).append(':')

fun <T> Appendable.appendCode(indexed: IndexedValue<T>, appendValueFn: Appendable.(T) -> Appendable) =
	this
		.appendIndexCode(indexed.index)
		.appendValueFn(indexed.value)

fun <T> Appendable.appendCode(array: Array1<T>, appendValueFn: Appendable.(T) -> Appendable) =
	foldIndexed(array) {
		appendCode(it, appendValueFn)
	}

fun <T> Appendable.appendCode(stack: Stack32<T>, appendValueFn: Appendable.(T) -> Appendable) =
	foldIndexed(stack.array32) {
		appendValueFn(it.value)
	}

fun Appendable.appendCode(function: Function) =
	appendLineCode("function") {
		appendCode(function.matchArray) {
			appendCode(it)
		}
	}

fun Appendable.appendCode(match: Match): Appendable =
	appendLineCode("match") {
		when (match) {
			is PartialMatch -> appendCode(match.function)
			is OpMatch -> appendCode(match.op)
		}
	}

fun Appendable.appendCode(op: Op): Appendable =
	appendLineCode("op") {
		when (op) {
			is LogOp -> appendCode(op.log)
			is PushOp -> appendCode(op.push)
			is PopOp -> appendCode(op.pop)
			is NandOp -> appendCode(op.nand)
		}
	}

fun Appendable.appendCode(log: Log) =
	appendLineCode("log") {
		appendCode(log.tag)
	}

fun Appendable.appendCode(tag: Tag) =
	appendLineCode("tag") {
		appendCode(tag.string)
	}

fun Appendable.appendCode(push: Push) =
	appendSimpleLineCode("push")

fun Appendable.appendCode(pop: Pop) =
	appendSimpleLineCode("pop")

fun Appendable.appendCode(nand: Nand) =
	appendSimpleLineCode("nand")

fun Appendable.appendCode(runtime: Runtime) =
	appendLineCode("runtime") {
		this
			.appendLineCode("scope") {
				appendCode(runtime.scopeFunction)
			}
			.appendLineCode("bits") {
				appendCode(runtime.bitStack) {
					appendCode(it.int)
				}
			}
			.appendLineCode("functions") {
				appendCode(runtime.functionStack) {
					appendCode(it)
				}
			}
	}

fun Appendable.appendCode(leo: Leo) =
	appendLineCode("leo") {
		appendCode(leo.runtime)
	}

fun Appendable.appendSimpleLineCode(word: String) =
	appendLineCode(word) { this }

fun Appendable.appendLineCode(word: String, appendScriptFn: Appendable.() -> Appendable) =
	appendScriptCode {
		appendWordCode(word).appendScriptFn()
	}

fun Appendable.appendScriptCode(appendScriptFn: Appendable.() -> Appendable) =
	appendScriptFn().appendEndCode
