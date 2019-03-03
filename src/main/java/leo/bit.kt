package leo

import leo.binary.Bit

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

// === parsers

val Bit.inc: Bit?
	get() =
		when (this) {
			Bit.ZERO -> Bit.ONE
			Bit.ONE -> null
		}

val Bit.carryInc: CarryAnd<Bit>
	get() =
		when (this) {
			Bit.ZERO -> Bit.ZERO.carry.and(Bit.ONE)
			Bit.ONE -> Bit.ONE.carry.and(Bit.ZERO)
		}
