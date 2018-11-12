package leo

import leo.base.orNull
import leo.base.string

data class Repl(
	val reader: Reader,
	val evaluator: Evaluator
	//, val printer: Printer
) {
	override fun toString() = reflect.string
}

val emptyRepl =
	Repl(emptyReader, emptyEvaluator)

fun Repl.push(byte: Byte): Repl? =
	evaluator.orNull.read(reader, byte, { readerScript ->
		evaluator.scopeStack.top.function.invoke(readerScript)
	}) { evaluatorOrNull, nextByte ->
		evaluatorOrNull?.push(nextByte)
	}?.let { (newEvaluatorOrNull, newReader) ->
		newEvaluatorOrNull?.let { newEvaluator ->
			Repl(newReader, newEvaluator)
		}
	}

val Repl.evaluatedScript: Script?
	get() =
		evaluator.evaluatedScript

// === utils

fun Repl.push(byteArray: ByteArray): Repl? =
	byteArray.fold(orNull) { replOrNull, byte ->
		replOrNull?.push(byte)
	}

fun Repl.push(string: String): Repl? =
	push(string.toByteArray())

// === reflect

val Repl.reflect: Field<Nothing>
	get() =
		replWord fieldTo term(
			reader.reflect,
			evaluator.reflect)

fun <R> R.foldBytes(repl: Repl, fn: R.(Byte) -> R): R =
	foldBytes(repl.evaluator, fn)