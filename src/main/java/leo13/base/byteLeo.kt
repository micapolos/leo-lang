package leo13.base

import leo13.scripter.Scripter
import leo13.scripter.field
import leo13.scripter.scripter
import leo13.scripter.toString

data class ByteLeo(val byte: Byte) {
	override fun toString() = byteScripter.toString(byte)
}

fun leo(byte: Byte) = ByteLeo(byte)

val byteScripter: Scripter<Byte> =
	scripter(
		"byte",
		field(scripter("first", bitScripter)) { bit7 },
		field(scripter("second", bitScripter)) { bit6 },
		field(scripter("third", bitScripter)) { bit5 },
		field(scripter("fourth", bitScripter)) { bit4 },
		field(scripter("fifth", bitScripter)) { bit3 },
		field(scripter("sixth", bitScripter)) { bit2 },
		field(scripter("seventh", bitScripter)) { bit1 },
		field(scripter("eight", bitScripter)) { bit0 })
	{ bit7, bit6, bit5, bit4, bit3, bit2, bit1, bit0 ->
		byte(bit7, bit6, bit5, bit4, bit3, bit2, bit1, bit0)
	}

fun byte(bit7: Bit, bit6: Bit, bit5: Bit, bit4: Bit, bit3: Bit, bit2: Bit, bit1: Bit, bit0: Bit): Byte =
	0
		.or(bit7.int.shl(7))
		.or(bit6.int.shl(6))
		.or(bit5.int.shl(5))
		.or(bit4.int.shl(4))
		.or(bit3.int.shl(3))
		.or(bit2.int.shl(2))
		.or(bit1.int.shl(1))
		.or(bit0.int.shl(0))
		.toByte()

val Byte.bit7: Bit get() = isOneBit(toInt().and(0x80) != 0)
val Byte.bit6: Bit get() = isOneBit(toInt().and(0x40) != 0)
val Byte.bit5: Bit get() = isOneBit(toInt().and(0x20) != 0)
val Byte.bit4: Bit get() = isOneBit(toInt().and(0x10) != 0)
val Byte.bit3: Bit get() = isOneBit(toInt().and(0x08) != 0)
val Byte.bit2: Bit get() = isOneBit(toInt().and(0x04) != 0)
val Byte.bit1: Bit get() = isOneBit(toInt().and(0x02) != 0)
val Byte.bit0: Bit get() = isOneBit(toInt().and(0x01) != 0)

fun byte(int: Int) = leo.base.byte(int)