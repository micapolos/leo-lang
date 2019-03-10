package leo

import leo.base.EnumBit

val EnumBit.reflect: Field<Nothing>
	get() =
		bitWord fieldTo
			when (this) {
				EnumBit.ZERO -> zeroWord
				EnumBit.ONE -> oneWord
			}.term

val Field<Nothing>.parseBit: EnumBit?
	get() =
		when {
			this == bitWord fieldTo zeroWord.term -> EnumBit.ZERO
			this == bitWord fieldTo oneWord.term -> EnumBit.ONE
			else -> null
		}

// === parsers

val EnumBit.inc: EnumBit?
	get() =
		when (this) {
			EnumBit.ZERO -> EnumBit.ONE
			EnumBit.ONE -> null
		}

val EnumBit.carryInc: CarryAnd<EnumBit>
	get() =
		when (this) {
			EnumBit.ZERO -> EnumBit.ZERO.carry.and(EnumBit.ONE)
			EnumBit.ONE -> EnumBit.ONE.carry.and(EnumBit.ZERO)
		}
