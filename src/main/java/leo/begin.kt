package leo

import leo.base.*

object Begin

val begin = Begin

val beginByte: Byte =
	'('.clampedByte

val beginChar: Char =
	beginByte.char

val beginString: String =
	beginChar.toString()

val Byte.beginOrNull: Begin?
	get() =
		if (this == beginByte) begin
		else null

val beginBitStream
	get() =
		beginByte.bitStream

val Stream<Bit>.bitParseBegin: Parse<Bit, Begin>?
	get() =
		bitParseByte?.let { bitParseByte ->
			bitParseByte.parsed.beginOrNull?.let { begin ->
				bitParseByte.streamOrNull parsed begin
			}
		}
