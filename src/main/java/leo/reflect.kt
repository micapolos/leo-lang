package leo

import leo.base.*

val Bit.reflect: Field<Value>
	get() =
		bitWord fieldTo term(
			when (this) {
				Bit.ZERO -> zeroWord
				Bit.ONE -> oneWord
			}
		)

val Byte.reflect: Field<Value>
	get() =
		byteWord fieldTo bitStack.reflect(Bit::reflect)

val ByteArray.reflect: Field<Value>
	get() =
		byteWord fieldTo
			fold(nullStack<Field<Value>>()) { fieldStack, byte ->
				fieldStack.push(byte.reflect)
			}.let { fieldStackOrNull ->
				if (fieldStackOrNull == null) term<Value>(arrayWord)
				else term(arrayWord fieldTo fieldStackOrNull.term)
			}
