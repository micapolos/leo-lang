package leo32.treo

import leo.base.Seq
import leo.base.applyEach
import leo.base.charSeq
import leo.base.map
import leo.binary.Bit
import leo.binary.digitBitOrNull

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

fun Scope.putBit(bitSeq: Seq<Bit>) =
	applyEach(bitSeq, Scope::put)

fun Scope.putBit(string: String) =
	applyEach(string.charSeq.map { digitBitOrNull!! }, Scope::put)

val voidScope = scope(voidSink)

fun scopeString(fn: Scope.() -> Unit) =
	sinkString { scope(this).fn() }

