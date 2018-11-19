package leo

import leo.base.*

data class Repl(
	val isError: Boolean,
	val bitReader: BitReader,
	val errorBitWriter: Writer<Bit>)

fun emptyRepl(errorBitWriter: Writer<Bit>): Repl =
	Repl(false, emptyBitReader, errorBitWriter)

fun Repl.read(bit: Bit): Repl =
	if (isError) copy(errorBitWriter = errorBitWriter.write(bit))
	else bitReader.read(bit).let { nextBitPreprocessor ->
		if (nextBitPreprocessor == null) copy(
			isError = true,
			errorBitWriter = errorBitWriter
				.write(bitReader.bitStreamOrNull)
				// TODO(micapolos): It may be byte-misaligned. How to solve this?
				.write("<<<ERROR>>>".bitStreamOrNull)
				.write(bit))
		else copy(bitReader = nextBitPreprocessor)
	}

val Repl.bitStreamOrNull: Stream<Bit>?
	get() =
		if (!isError) bitReader.bitStreamOrNull else null

fun runRepl(inputBitStream: Stream<Bit>?, outBitWriter: Writer<Bit>, errorBitWriter: Writer<Bit>) {
	emptyRepl(errorBitWriter)
		.fold(inputBitStream, Repl::read)
		.bitStreamOrNull
		?.let { bitStream -> outBitWriter.write(bitStream) }
}
