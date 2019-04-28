package leo32.treo

import leo.base.seq
import leo.binary.Bit
import leo.binary.digitChar

val Bit.charSeq
	get() =
		seq(digitChar)