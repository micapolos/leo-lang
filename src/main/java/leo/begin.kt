package leo

import leo.base.Bit
import leo.base.Stream
import leo.base.bitStream
import leo.base.clampedByte

val beginByte: Byte =
	'('.clampedByte

val beginBitStream: Stream<Bit>
	get() =
		beginByte.bitStream