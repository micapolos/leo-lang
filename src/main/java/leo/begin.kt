package leo

import leo.base.Bit
import leo.base.Stream
import leo.base.bitStream
import leo.base.clampedByte

val beginBitStream: Stream<Bit>
	get() =
		'('.clampedByte.bitStream