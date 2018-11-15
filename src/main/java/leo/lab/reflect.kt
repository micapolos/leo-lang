package leo.lab

import leo.base.Bit
import leo.base.bitStack
import leo.bitWord
import leo.byteWord
import leo.oneWord
import leo.zeroWord

val Bit.reflect: Field<Nothing>
	get() =
		bitWord fieldTo
			when (this) {
				Bit.ZERO -> zeroWord
				Bit.ONE -> oneWord
			}.term

val Byte.reflect: Field<Nothing>
	get() =
		byteWord fieldTo bitStack.reflect(Bit::reflect)
