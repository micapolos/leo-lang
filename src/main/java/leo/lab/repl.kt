package leo.lab

import leo.base.Bit
import leo.base.Writer

data class Repl(
	val isError: Boolean,
	val bitPreprocessor: BitPreprocessor,
	val errorBitWriter: Writer<Bit, *>)

//fun emptyRepl(errorBitWriter: Writer<Bit, Unit>): Repl =
//	Repl(false, emptyBitPreprocessor, errorBitWriter)
//
//fun Repl.read(bit: Bit): Repl =
//	if (isError) copy(errorBitWriter = errorBitWriter.write(bit))
//	else bitPreprocessor.plus(bit).let { nextBitPreprocessor ->
//		if (nextBitPreprocessor == null) copy(isError = true, errorBitWriter = errorBitWriter.write(bit))
//		else copy(bitPreprocessor = nextBitPreprocessor)
//	}
//
//val Repl.bitStreamOrNull: Stream<Bit>?
//	get() =
//		bitPreprocessor.bitSteamOrNull
//
//fun runRepl(inputBitStream: Stream<Bit>?, outBitWriter: Writer<Bit, *>, errorBitWriter: Writer<Bit, *>) {
//	emptyRepl(errorBitWriter)
//		.fold(inputBitStream, Repl::read)
//		.bitStreamOrNull
//		?.let { bitStream -> outBitWriter.write(bitStream) }
//}
