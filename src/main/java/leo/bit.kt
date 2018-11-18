package leo

import leo.base.Bit

val Bit.reflect: Field<Nothing>
	get() =
		bitWord fieldTo
			when (this) {
				Bit.ZERO -> zeroWord
				Bit.ONE -> oneWord
			}.term

val Field<Nothing>.parseBit: Bit?
	get() =
		when {
			this == bitWord fieldTo zeroWord.term -> Bit.ZERO
			this == bitWord fieldTo oneWord.term -> Bit.ONE
			else -> null
		}
