package leo32.treo

import leo.base.Seq
import leo.base.charSeq
import leo.base.fold
import leo.base.map
import leo.binary.Bit
import leo.binary.digitBitOrNull
import leo32.c.Mem

data class Executor(
	val mem: Mem,
	var currentTreo: Treo)

fun executor(mem: Mem, treo: Treo) =
	Executor(mem, treo)

fun Executor.plus(bit: Bit) =
	apply { currentTreo = currentTreo.invoke(bit) }

fun Executor.plusBit(bitSeq: Seq<Bit>) =
	fold(bitSeq, Executor::plus)

fun Executor.plusBit(string: String) =
	fold(string.charSeq.map { digitBitOrNull!! }, Executor::plus)

val Executor.bitString
	get() =
		currentTreo.bitString
