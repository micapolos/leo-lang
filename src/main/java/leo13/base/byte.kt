package leo13.base

import leo13.fail
import leo13.script.*
import leo13.script.Writer
import leo9.map8OrNull

val byteName = "byte"

val byteReader: Reader<Byte> =
	reader(byteName) {
		lineStack.map8OrNull { l7, l6, l5, l4, l3, l2, l1, l0 ->
			byte(
				bitReader.unsafeValue(l7),
				bitReader.unsafeValue(l6),
				bitReader.unsafeValue(l5),
				bitReader.unsafeValue(l4),
				bitReader.unsafeValue(l3),
				bitReader.unsafeValue(l2),
				bitReader.unsafeValue(l1),
				bitReader.unsafeValue(l0))
		} ?: fail("byte")
	}

val byteWriter: Writer<Byte> =
	writer(byteName) {
		script(
			bitWriter.scriptLine(bit7),
			bitWriter.scriptLine(bit6),
			bitWriter.scriptLine(bit5),
			bitWriter.scriptLine(bit4),
			bitWriter.scriptLine(bit3),
			bitWriter.scriptLine(bit2),
			bitWriter.scriptLine(bit1),
			bitWriter.scriptLine(bit0))
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