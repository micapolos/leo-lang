package leo13.base.typed

import leo.base.byte
import leo.binary.*
import leo13.base.Typed
import leo13.base.type.*

val byteType: Type<Byte> =
	type(
		"byte",
		body(
			struct(
				field(bitType) { bit7 },
				field(bitType) { bit6 },
				field(bitType) { bit5 },
				field(bitType) { bit4 },
				field(bitType) { bit3 },
				field(bitType) { bit2 },
				field(bitType) { bit1 },
				field(bitType) { bit0 }
			) { bit7, bit6, bit5, bit4, bit3, bit2, bit1, bit0 ->
				byte(bit7, bit6, bit5, bit4, bit3, bit2, bit1, bit0)
			}))

data class ByteTyped(val byte: Byte) : Typed<Byte>() {
	override fun toString() = super.toString()
	override val type = byteType
}

