package leo.base

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