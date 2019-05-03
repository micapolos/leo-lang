package leo32.treo

import leo.base.Seq
import leo.base.charSeq
import leo.base.fold
import leo.base.map
import leo.binary.Bit
import leo.binary.digitBitOrNull

data class Executor(
	val outputSink: Sink,
	var currentTreo: Treo)

fun executor(treo: Treo) =
	voidSink.executor(treo)

fun Sink.executor(treo: Treo) =
	Executor(this, treo)

fun Executor.plus(bit: Bit) =
	apply { currentTreo = currentTreo.invoke(bit) }

fun Executor.plusBit(bitSeq: Seq<Bit>) =
	fold(bitSeq, Executor::plus)

fun Executor.plusBit(string: String) =
	fold(string.charSeq.map { digitBitOrNull!! }, Executor::plus)

val Executor.bitString
	get() =
		currentTreo.bitString

val Executor.sink
	get() =
		Sink { currentTreo = currentTreo.invoke(this, outputSink) }
