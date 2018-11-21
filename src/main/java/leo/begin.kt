package leo

import leo.base.*

object Begin

val begin = Begin

val beginByte: Byte =
	'('.clampedByte

val Byte.beginOrNull: Begin?
	get() =
		if (this == beginByte) begin
		else null

val Stream<Bit>.bitParseBegin: Parse<Bit, Begin>?
	get() =
		bitParseByte?.let { bitParseByte ->
			bitParseByte.parsed.beginOrNull?.let { begin ->
				bitParseByte.streamOrNull parsed begin
			}
		}
