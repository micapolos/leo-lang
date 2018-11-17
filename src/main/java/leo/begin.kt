package leo

import leo.base.Bit
import leo.base.Stream
import leo.base.bitStream

val beginBitStream: Stream<Bit>
	get() =
		'('.toByte().bitStream