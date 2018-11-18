package leo

import leo.base.*

val Byte.reflect: Field<Nothing>
	get() =
		byteWord fieldTo bitStack.reflect(Bit::reflect)

val Field<Nothing>.parseByte: Byte?
	get() =
		if (word != byteWord) null
		else termOrNull
			?.parseStack { field -> field.parseBit }
			?.let { bitStack ->
				if (bitStack.sizeInt != 8) null
				else 0x00.fold(bitStack.reverse.stream) { bit ->
					shl(1).or(bit.int)
				}.clampedByte
			}
