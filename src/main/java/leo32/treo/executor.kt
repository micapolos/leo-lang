package leo32.treo

import leo.base.charSeq
import leo.base.charString
import leo.base.fold
import leo.base.map
import leo.binary.Bit
import leo.binary.digitBitOrNull

data class Executor(
	var currentTreo: Treo)

fun executor(treo: Treo) =
	Executor(treo)

fun Executor.plus(bit: Bit) =
	apply { currentTreo = currentTreo.invoke(bit) }

fun Executor.plus(string: String) =
	fold(string.charSeq.map { digitBitOrNull!! }, Executor::plus)

val Executor.valueString
	get() =
		currentTreo.enteredCharSeq.charString
