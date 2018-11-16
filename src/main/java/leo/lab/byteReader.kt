package leo.lab

import leo.*
import leo.base.Stream
import leo.base.orNullThenIfNotNull

data class ByteReader(
	val tokenReader: TokenReader,
	val wordOrNull: Word?)

// === constructors

val emptyByteReader =
	ByteReader(emptyTokenReader, null)

// === mutation

fun ByteReader.read(byte: Byte): ByteReader? =
	byte.toChar().let { char ->
		when (char) {
			'(' -> begin
			')' -> end
			else -> char.letterOrNull?.let(this::plus)
		}
	}

val ByteReader.begin: ByteReader?
	get() =
		if (wordOrNull == null) null
		else ByteReader(tokenReader.begin(wordOrNull), null)

val ByteReader.end: ByteReader?
	get() =
		if (wordOrNull != null) null
		else tokenReader.end?.let { endedTokenReader ->
			ByteReader(endedTokenReader, null)
		}

fun ByteReader.plus(letter: Letter): ByteReader =
	ByteReader(tokenReader, wordOrNull.plus(letter))

// === byte stream

val ByteReader.byteStreamOrNull: Stream<Byte>?
	get() =
		tokenReader.byteStreamOrNull
			.orNullThenIfNotNull(wordOrNull?.byteStream)