package leo

import leo.base.Bit
import leo.base.Stream
import leo.base.bitStream
import leo.base.clampedByte

val endByte: Byte =
	')'.clampedByte

val endBitStream: Stream<Bit>
	get() =
		endByte.bitStream