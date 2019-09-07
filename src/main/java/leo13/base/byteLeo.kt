package leo13.base

import leo.base.byte
import leo.binary.*
import leo13.base.type.Type
import leo13.base.type.field
import leo13.base.type.toString
import leo13.base.type.type

val byteType: Type<Byte> =
	type(
		"byte",
		field(bitType) { bit7 },
		field(bitType) { bit6 },
		field(bitType) { bit5 },
		field(bitType) { bit4 },
		field(bitType) { bit3 },
		field(bitType) { bit2 },
		field(bitType) { bit1 },
		field(bitType) { bit0 })
	{ bit7, bit6, bit5, bit4, bit3, bit2, bit1, bit0 ->
		byte(bit7, bit6, bit5, bit4, bit3, bit2, bit1, bit0)
	}

data class ByteLeo(val byte: Byte) {
	override fun toString() = byteType.toString(byte)
}

fun leo(byte: Byte) = ByteLeo(byte)
