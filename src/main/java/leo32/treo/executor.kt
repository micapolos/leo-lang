package leo32.treo

import leo.base.Seq
import leo.base.charSeq
import leo.base.fold
import leo.base.map
import leo.binary.Bit
import leo.binary.digitBitOrNull

data class Executor(
	val parentScope: Scope,
	var currentTreo: Treo)

fun executor(scope: Scope, treo: Treo) =
	Executor(scope, treo)

fun Executor.put(bit: Bit) =
	apply { currentTreo = currentTreo.invoke(bit, parentScope) }

fun Executor.plusBit(bitSeq: Seq<Bit>) =
	fold(bitSeq, Executor::put)

fun Executor.plusBit(string: String) =
	fold(string.charSeq.map { digitBitOrNull!! }, Executor::put)

val Executor.bitString
	get() =
		currentTreo.bitString
