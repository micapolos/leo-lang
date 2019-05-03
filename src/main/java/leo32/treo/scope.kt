package leo32.treo

import leo.binary.Bit

sealed class Scope

data class SinkScope(
	val sink: Sink) : Scope()

data class ExecutorScope(
	val executor: Executor) : Scope()

fun scope(sink: Sink) = SinkScope(sink)
fun scope(executor: Executor) = ExecutorScope(executor)

fun Scope.invoke(put: Put) =
	put(put.value.bit)

fun Scope.put(bit: Bit) {
	when (this) {
		is ExecutorScope -> executor.put(bit)
		is SinkScope -> sink.put(bit)
	}
}

val voidScope = scope(voidSink)
