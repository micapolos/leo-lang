package leo

import leo.base.Bit
import leo.base.Stream
import leo.base.bitStream

val endBitStream: Stream<Bit>
	get() =
		')'.toByte().bitStream