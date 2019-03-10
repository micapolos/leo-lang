package leo

import leo.base.*

data class Repl(
	val isError: Boolean,
	val bitReader: Reader<EnumBit>,
	val errorBitWriter: Writer<EnumBit>)

fun emptyRepl(errorBitWriter: Writer<EnumBit>): Repl =
	Repl(false, emptyBitReader, errorBitWriter)

fun Repl.read(bit: EnumBit): Repl =
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

val Repl.bitStreamOrNull: Stream<EnumBit>?
	get() =
		if (!isError) bitReader.bitStreamOrNull else null

fun runRepl(inputBitStream: Stream<EnumBit>?, outBitWriter: Writer<EnumBit>, errorBitWriter: Writer<EnumBit>) {
	emptyRepl(errorBitWriter)
		.fold(inputBitStream, Repl::read)
		.bitStreamOrNull
		.let { bitStream ->
			outBitWriter.write(bitStream).write(newlineBitStream)
		}
}
