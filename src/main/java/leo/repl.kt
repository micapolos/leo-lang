package leo

import leo.base.orNull

data class Repl(
	val reader: Reader,
	val evaluator: Evaluator
	//, val printer: Printer
)

val emptyRepl =
	Repl(emptyReader, emptyEvaluator)

fun Repl.push(byte: Byte): Repl? =
	reader
		.read(evaluator.orNull, byte) { evaluatorOrNull, nextByte ->
			evaluatorOrNull?.push(nextByte)
		}?.let { (newEvaluatorOrNull, newReader) ->
			newEvaluatorOrNull?.let { newEvaluator ->
				Repl(newReader, newEvaluator)
			}
		}

val Repl.evaluatedScript: Script?
	get() =
		evaluator.evaluatedScript

fun Repl.push(byteArray: ByteArray): Repl? =
	byteArray.fold(orNull) { leoOrNull, byte ->
		leoOrNull?.push(byte)
	}

fun Repl.push(string: String): Repl? =
	string.toByteArray().fold(orNull) { leoOrNull, byte ->
		leoOrNull?.push(byte)
	}

