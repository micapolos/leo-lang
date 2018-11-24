package leo

import leo.base.*

object End

val end = End

val endByte: Byte =
	')'.clampedByte

val endBitStream =
	endByte.bitStream

val Byte.endOrNull: End?
	get() =
		if (this == endByte) end
		else null

val Stream<Bit>.bitParseEnd: Parse<Bit, End>?
	get() =
		bitParseByte?.let { bitParseByte ->
			bitParseByte.parsed.endOrNull?.let { end ->
				bitParseByte.streamOrNull parsed end
			}
		}
