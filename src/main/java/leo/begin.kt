package leo

import leo.base.Bit
import leo.base.Stream
import leo.base.bitStream
import leo.base.byte

val beginBitStream: Stream<Bit>
	get() =
		'('.byte.bitStream