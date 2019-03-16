@file:Suppress("UNUSED_PARAMETER")

package leo32

import leo.base.ifNotNull
import leo.base.indexed
import leo.binary.Arr1
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

fun <T> Appendable.appendCode(array: Arr1<T>, appendValueFn: Appendable.(T) -> Appendable) = this
	.ifNotNull(array.at0) { appendCode(0 indexed it, appendValueFn) }
	.ifNotNull(array.at0) { appendCode(1 indexed it, appendValueFn) }

fun <T> Appendable.appendCode(stack: Stack32<T>, appendValueFn: Appendable.(T) -> Appendable) =
	foldIndexed(stack) {
		appendCode(it, appendValueFn)
	}

fun <T> Appendable.appendCode(function: Function<T>) =
	appendLineCode("function") {
		appendCode(function.matchMap) {
			ifNotNull(it) {
				appendCode(it)
			}
		}
	}

fun <T> Appendable.appendCode(match: Match<T>): Appendable =
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

fun <T> Appendable.appendCode(op: Op<T>): Appendable =
	appendLineCode("op") {
		when (op) {
			is LogOp -> appendCode(op.log)
			is PushOp -> appendCode(op.push)
			is PopOp -> appendCode(op.pop)
			is NandOp -> appendCode(op.nand)
			is SeqOp -> appendCode(op)
			is OutOp -> appendCode(op.out)
		}
	}

fun Appendable.appendCode(seqOp: SeqOp<*>) =
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

fun Appendable.appendCode(out: Out) =
	appendSimpleLineCode("out")

fun <T> Appendable.appendCode(runtime: Runtime<T>) =
	appendLineCode("runtime") {
		runtime.appendCodeFn.invoke(this, runtime.state)
	}

fun <T> Appendable.appendCode(scope: Scope<T>) =
	appendLineCode("scope") {
		this
			.appendCode(scope.runtime)
			.appendLineCode("bits") {
				appendCode(scope.bitStack) {
					appendCode(it.int)
				}
			}
			.appendLineCode("scope") {
				appendCode(scope.scopeFunction)
			}
			.appendLineCode("current") {
				appendCode(scope.currentFunction)
			}
	}

fun Appendable.appendSimpleLineCode(word: String) =
	appendLineCode(word) { this }

fun Appendable.appendLineCode(word: String, appendScriptFn: Appendable.() -> Appendable) =
	appendScriptCode {
		appendWordCode(word).appendScriptFn()
	}

fun Appendable.appendScriptCode(appendScriptFn: Appendable.() -> Appendable) =
	appendScriptFn().appendEndCode
