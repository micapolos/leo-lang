package leo

import leo.base.Stream
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
	evaluator.orNull.read(reader, byte, { readerValueTerm ->
		evaluator.scopeStack.top.function.invoke(readerValueTerm)
	}) { nextByte ->
		this?.push(nextByte)
	}?.let { (newEvaluatorOrNull, newReader) ->
		newEvaluatorOrNull?.let { newEvaluator ->
			Repl(newReader, newEvaluator)
		}
	}

val Repl.evaluatedValueTerm: Term<Value>?
	get() =
		evaluator.evaluatedValueTerm

// === utils

fun Repl.push(byteArray: ByteArray): Repl? =
	byteArray.fold(orNull) { replOrNull, byte ->
		replOrNull?.push(byte)
	}

fun Repl.push(string: String): Repl? =
	push(string.toByteArray())

// === reflect

val Repl.reflect: Field<Value>
	get() =
		replWord fieldTo term(
			reader.reflect,
			evaluator.reflect)

val Repl.byteStreamOrNull: Stream<Byte>?
	get() =
		evaluator.byteStreamOrNull
