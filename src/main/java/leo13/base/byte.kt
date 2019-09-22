package leo13.base

import leo13.byteName
import leo13.script.*
import leo13.script.Writer

val byteReader: Reader<Byte> =
	reader(
		byteName,
		reader("first", bitReader),
		reader("second", bitReader),
		reader("third", bitReader),
		reader("fourth", bitReader),
		reader("fifth", bitReader),
		reader("sixth", bitReader),
		reader("seventh", bitReader),
		reader("eight", bitReader),
		::byte)

val byteWriter: Writer<Byte> =
	writer(
		byteName,
		field(writer("first", bitWriter)) { bit7 },
		field(writer("second", bitWriter)) { bit6 },
		field(writer("third", bitWriter)) { bit5 },
		field(writer("fourth", bitWriter)) { bit4 },
		field(writer("fifth", bitWriter)) { bit3 },
		field(writer("sixth", bitWriter)) { bit2 },
		field(writer("seventh", bitWriter)) { bit1 },
		field(writer("eight", bitWriter)) { bit0 })

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