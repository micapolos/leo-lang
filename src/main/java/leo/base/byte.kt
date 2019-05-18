package leo.base

import leo.binary.Zero
import leo.binary.bit

fun byte(int: Int) = int.toByte()

// TODO: Check if it's properly inlined in java.
fun byte(bit7: EnumBit, bit6: EnumBit, bit5: EnumBit, bit4: EnumBit, bit3: EnumBit, bit2: EnumBit, bit1: EnumBit, bit0: EnumBit): Byte = 0
	.or(bit7.int.shl(7))
	.or(bit6.int.shl(6))
	.or(bit5.int.shl(5))
	.or(bit4.int.shl(4))
	.or(bit3.int.shl(3))
	.or(bit2.int.shl(2))
	.or(bit1.int.shl(1))
	.or(bit0.int.shl(0)).clampedByte

inline val Byte.bit7: EnumBit
	get() =
		int.and(0x80).clampedEnumBit

inline val Byte.bit6: EnumBit
	get() =
		int.and(0x40).clampedEnumBit

inline val Byte.bit5: EnumBit
	get() =
		int.and(0x20).clampedEnumBit

inline val Byte.bit4: EnumBit
	get() =
		int.and(0x10).clampedEnumBit

inline val Byte.bit3: EnumBit
	get() =
		int.and(0x08).clampedEnumBit

inline val Byte.bit2: EnumBit
	get() =
		int.and(0x04).clampedEnumBit

inline val Byte.bit1: EnumBit
	get() =
		int.and(0x02).clampedEnumBit

inline val Byte.bit0: EnumBit
	get() =
		int.and(0x01).clampedEnumBit

val Byte.int
	get() =
		toInt()

val Byte.uint
	get() =
		int.and(0xFF)

val Byte.char
	get() =
		toChar()

val Byte.bitStream: Stream<EnumBit>
	get() =
		bit7.onlyStream.then {
			bit6.onlyStream.then {
				bit5.onlyStream.then {
					bit4.onlyStream.then {
						bit3.onlyStream.then {
							bit2.onlyStream.then {
								bit1.onlyStream.then {
									bit0.onlyStream
								}
							}
						}
					}
				}
			}
		}

val Stream<Byte>.byteBitStream: Stream<EnumBit>
	get() =
		map(Byte::bitStream).join

val Stream<EnumBit>.bitByteStreamOrNull: Stream<Byte>?
	get() =
		bitParseByte?.let { parse ->
			parse.parsed.onlyStreamThen { parse.streamOrNull?.bitByteStreamOrNull }
		}

val Stream<EnumBit>.bitParseByte: Parse<EnumBit, Byte>?
	get() =
		matchFirst { bit7 ->
			this?.matchFirst { bit6 ->
				this?.matchFirst { bit5 ->
					this?.matchFirst { bit4 ->
						this?.matchFirst { bit3 ->
							this?.matchFirst { bit2 ->
								this?.matchFirst { bit1 ->
									this?.matchFirst { bit0 ->
										parsed(byte(bit7, bit6, bit5, bit4, bit3, bit2, bit1, bit0))
									}
								}
							}
						}
					}
				}
			}
		}

fun Byte.wrapPlus(byte: Byte) =
	plus(byte).clampedByte

// === byte <-> int conversion

fun short(byte1: Byte, byte0: Byte) =
	byte1.uint.shl(8).or(byte0.uint).clampedShort

val Short.byte1 get() = uint.shr(8).clampedByte
val Short.byte0 get() = uint.clampedByte

fun Short.updateByte1(fn: Byte.() -> Byte) = short(byte0, byte1.fn())
fun Short.updateByte0(fn: Byte.() -> Byte) = short(byte0.fn(), byte1)

fun Short.setByte1(byte1: Byte) = updateByte1 { byte1 }
fun Short.setByte0(byte0: Byte) = updateByte0 { byte0 }

val Int.byte3 get() = shr(24).and(0xff).clampedByte
val Int.byte2 get() = shr(16).and(0xff).clampedByte
val Int.byte1 get() = shr(8).and(0xff).clampedByte
val Int.byte0 get() = and(0xff).clampedByte

inline fun Int.updateByte3(fn: Byte.() -> Byte) = byte3.fn().int.and(0xFF).shl(24).or(and(0x00FFFFFF))
inline fun Int.updateByte2(fn: Byte.() -> Byte) = byte2.fn().int.and(0xFF).shl(16).or(and(0xFF00FFFF.toInt()))
inline fun Int.updateByte1(fn: Byte.() -> Byte) = byte1.fn().int.and(0xFF).shl(8).or(and(0xFFFF00FF.toInt()))
inline fun Int.updateByte0(fn: Byte.() -> Byte) = byte0.fn().int.and(0xFF).or(and(0xFFFFFF00.toInt()))

fun Int.setByte3(byte: Byte) = updateByte3 { byte }
fun Int.setByte2(byte: Byte) = updateByte2 { byte }
fun Int.setByte1(byte: Byte) = updateByte1 { byte }
fun Int.setByte0(byte: Byte) = updateByte0 { byte }

fun Int.byte(index: Int): Byte =
	ushr(index.shl(3)).clampedByte

fun Int.setByte(index: Int, byte: Byte): Int =
	index.shl(3).let { shift ->
		0xFF.shl(shift).let { mask ->
			and(mask.inv()).or(byte.int.shl(shift).and(mask))
		}
	}

fun Int.updateByte(index: Int, fn: Byte.() -> Byte): Int =
	setByte(index, byte(index).fn())

val Byte.ushr1
	get() =
		int.and(0xFF).ushr(1).clampedByte

@Suppress("unused")
val Zero.byte
	get() = 0.clampedByte

val Byte.isZero get() = this == byte(0)
val Byte.bit get() = isZero.not().bit