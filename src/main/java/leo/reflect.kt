package leo

import leo.base.*

val Bit.reflect: Field<Nothing>
	get() =
		bitWord fieldTo term(
			when (this) {
				Bit.ZERO -> zeroWord
				Bit.ONE -> oneWord
			}
		)

val Byte.reflect: Field<Nothing>
	get() =
		byteWord fieldTo bitStack.reflect(Bit::reflect)

val ByteArray.reflect: Field<Nothing>
	get() =
		byteWord fieldTo
			fold(nullStack<Field<Nothing>>()) { fieldStack, byte ->
				fieldStack.push(byte.reflect)
			}.let { fieldStackOrNull ->
				if (fieldStackOrNull == null) term<Nothing>(arrayWord)
				else term(arrayWord fieldTo fieldStackOrNull.term)
			}