package leo

import leo.base.Parse
import leo.base.Stream
import leo.base.bitParseByte
import leo.base.parsed
import leo.binary.Bit

object End

val end = End
const val endChar = ')'
const val endByte = endChar.toByte()
const val endString = endChar.toString()

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
