package leo.lab

import leo.base.Bit
import leo.base.Stream
import leo.base.Writer
import leo.base.write

data class Repl(
	val isError: Boolean,
	val bitPreprocessor: BitPreprocessor,
	val errorBitWriter: Writer<Bit>)

fun Repl.read(bit: Bit) =
	if (isError) copy(errorBitWriter = errorBitWriter.write(bit))
	else bitPreprocessor.plus(bit)?.let { nextBitPreprocessor ->
		copy(bitPreprocessor = nextBitPreprocessor)
	}

val Repl.bitStreamOrNull: Stream<Bit>?
	get() =
		bitPreprocessor.bitSteamOrNull
