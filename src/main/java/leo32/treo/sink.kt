package leo32.treo

import leo.binary.Bit

typealias WriteFn = Bit.() -> Unit

data class Sink(
	val putFn: WriteFn)

fun Sink.put(bit: Bit) = putFn(bit)

val voidSink: Sink = Sink { Unit }

fun Sink.filter(treo: Treo): Sink =
	Executor(this, treo).sink
