package leo

import leo.base.Bit
import leo.base.Stream
import leo.base.bitStream
import leo.base.byte

val endBitStream: Stream<Bit>
	get() =
		')'.byte.bitStream