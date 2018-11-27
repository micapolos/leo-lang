package leo

import leo.base.*

data class Repl(
	val isError: Boolean,
	val bitReader: Reader<Bit>,
	val errorBitWriter: Writer<Bit>)

fun emptyRepl(errorBitWriter: Writer<Bit>): Repl =
	Repl(false, emptyBitReader, errorBitWriter)

fun Repl.read(bit: Bit): Repl =
	if (isError) copy(errorBitWriter = errorBitWriter.write(bit))
	else bitReader.read(bit).let { nextBitReader ->
		if (nextBitReader == null)
			copy(
				isError = true,
				errorBitWriter = errorBitWriter
					.write(bitReader.bitStreamOrNull)
					.write(bit)
					.write("<<<ERROR>>>".bitStreamOrNull))
		else copy(bitReader = nextBitReader)
	}

val Repl.bitStreamOrNull: Stream<Bit>?
	get() =
		if (!isError) bitReader.bitStreamOrNull else null

fun runRepl(inputBitStream: Stream<Bit>?, outBitWriter: Writer<Bit>, errorBitWriter: Writer<Bit>) {
	emptyRepl(errorBitWriter)
		.fold(inputBitStream, Repl::read)
		.run {
			bitStreamOrNull.let { bitStream ->
				outBitWriter.write(bitStream).write(newlineBitStream)
			}
		}
}
