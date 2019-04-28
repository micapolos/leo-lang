package leo32.treo

import leo.base.*
import leo.binary.Bit
import leo.binary.digitBitOrNull
import leo.binary.digitChar

data class Executor(
	var currentTreo: Treo)

fun executor(treo: Treo) =
	Executor(treo)

fun Executor.plus(bit: Bit) =
	apply { currentTreo = currentTreo.invoke(bit) }

fun Executor.plusBit(bitSeq: Seq<Bit>) =
	fold(bitSeq, Executor::plus)

fun Executor.plusBit(string: String) =
	fold(string.charSeq.map { digitBitOrNull!! }, Executor::plus)

val Executor.bitString
	get() =
		currentTreo.enteredBitSeq.map { digitChar }.charString
