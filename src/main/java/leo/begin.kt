package leo

import leo.base.*

object Begin

val begin = Begin

const val beginChar = '('
const val beginByte = beginChar.toByte()
const val beginString = beginChar.toString()

val Byte.beginOrNull: Begin?
	get() =
		if (this == beginByte) begin
		else null

val beginBitStream
	get() =
		beginByte.bitStream

val Stream<EnumBit>.bitParseBegin: Parse<EnumBit, Begin>?
	get() =
		bitParseByte?.let { bitParseByte ->
			bitParseByte.parsed.beginOrNull?.let { begin ->
				bitParseByte.streamOrNull parsed begin
			}
		}
