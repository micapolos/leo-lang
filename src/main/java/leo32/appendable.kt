@file:Suppress("UNUSED_PARAMETER")

package leo32

import leo.base.ifNotNull
import leo.base.indexed
import leo.binary.Map1
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

fun <T> Appendable.appendCode(array: Map1<T>, appendValueFn: Appendable.(T) -> Appendable) =
	foldIndexed(array) {
		appendCode(it, appendValueFn)
	}

fun <T> Appendable.appendCode(stack: Stack32<T>, appendValueFn: Appendable.(T) -> Appendable) =
	foldIndexed(stack.map32) {
		appendValueFn(it.value)
	}

fun Appendable.appendCode(function: Function) =
	appendLineCode("function") {
		appendCode(function.matchMap) {
			appendCode(it)
		}
	}

fun Appendable.appendCode(match: Match): Appendable =
	appendLineCode("match") {
		this
			.appendLineCode("op") {
				ifNotNull(match.opOrNull) {
					appendCode(it)
				}
			}
			.appendLineCode("next") {
				ifNotNull(match.nextFunctionOrNull) {
					appendCode(it)
				}
		}
	}

fun Appendable.appendCode(op: Op): Appendable =
	appendLineCode("op") {
		when (op) {
			is LogOp -> appendCode(op.log)
			is PushOp -> appendCode(op.push)
			is PopOp -> appendCode(op.pop)
			is NandOp -> appendCode(op.nand)
			is SeqOp -> appendCode(op)
		}
	}

fun Appendable.appendCode(seqOp: SeqOp) =
	this
		.appendCode(0 indexed seqOp.firstOp) { appendCode(it) }
		.appendCode(1 indexed seqOp.firstOp) { appendCode(it) }

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
			.appendLineCode("bits") {
				appendCode(runtime.bitStack) {
					appendCode(it.int)
				}
			}
			.appendLineCode("scope") {
				appendCode(runtime.scopeFunction)
			}
			.appendLineCode("current") {
				appendCode(runtime.currentFunction)
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
