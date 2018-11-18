package leo

import leo.base.Bit
import leo.base.Stream
import leo.base.bitStream
import leo.base.clampedByte

val endBitStream: Stream<Bit>
	get() =
		')'.clampedByte.bitStream