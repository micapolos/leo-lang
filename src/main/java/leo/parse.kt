package leo

import leo.base.*

val Field<Value>.parseBit: Bit?
	get() =
		when {
			this == bitWord fieldTo term<Value>(zeroWord) -> Bit.ZERO
			this == bitWord fieldTo term<Value>(oneWord) -> Bit.ONE
			else -> null
		}

val Field<Value>.parseByte: Byte?
	get() =
		if (key != byteWord) null
		else value
			.parseStack { field -> field.parseBit }
			?.let { bitStack ->
				if (bitStack.sizeInt != 8) null
				else 0x00.fold(bitStack.reverse) { bit ->
					shl(1).or(bit.int)
				}.toByte()
			}
