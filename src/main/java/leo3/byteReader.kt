package leo3

import leo.base.Seq
import leo.base.flatSeq
import leo.base.notNullIf
import leo.base.orEmptyIfNullSeq
import leo.binary.Bit

data class ByteReader(
	val wordReader: WordReader,
	val wordOrNull: Word?)

val WordReader.completedByteReader
	get() = ByteReader(this, null)

val ByteReader.isCompleted
	get() = wordOrNull == null

val ByteReader.completedWordReader
	get() = notNullIf(isCompleted) { wordReader }

fun ByteReader.read(byte: Byte): ByteReader? =
	wordOrNull
		.plus(byte)
		.let { wordOrNullPlusByte ->
			if (wordOrNullPlusByte == null) wordReader.read(wordOrNull)?.completedByteReader
			else copy(wordOrNull = wordOrNullPlusByte)
		}

val ByteReader.bitSeq: Seq<Bit>
	get() = flatSeq(wordReader.bitSeq, wordOrNull.orEmptyIfNullSeq { bitSeq })