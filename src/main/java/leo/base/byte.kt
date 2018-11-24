package leo.base

// TODO: Check if it's properly inlined in java.
fun byte(bit7: Bit, bit6: Bit, bit5: Bit, bit4: Bit, bit3: Bit, bit2: Bit, bit1: Bit, bit0: Bit): Byte = 0
	.or(bit7.int.shl(7))
	.or(bit6.int.shl(6))
	.or(bit5.int.shl(5))
	.or(bit4.int.shl(4))
	.or(bit3.int.shl(3))
	.or(bit2.int.shl(2))
	.or(bit1.int.shl(1))
	.or(bit0.int.shl(0)).clampedByte

inline val Byte.bit7: Bit
	get() =
		int.and(0x80).clampedBit

inline val Byte.bit6: Bit
	get() =
		int.and(0x40).clampedBit

inline val Byte.bit5: Bit
	get() =
		int.and(0x20).clampedBit

inline val Byte.bit4: Bit
	get() =
		int.and(0x10).clampedBit

inline val Byte.bit3: Bit
	get() =
		int.and(0x08).clampedBit

inline val Byte.bit2: Bit
	get() =
		int.and(0x04).clampedBit

inline val Byte.bit1: Bit
	get() =
		int.and(0x02).clampedBit

inline val Byte.bit0: Bit
	get() =
		int.and(0x01).clampedBit

val Byte.int
	get() =
		toInt()

val Byte.char
	get() =
		toChar()

val Byte.bitStream: Stream<Bit>
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

val Stream<Byte>.byteBitStream: Stream<Bit>
	get() =
		map(Byte::bitStream).join

val Stream<Bit>.bitByteStreamOrNull: Stream<Byte>?
	get() =
		bitParseByte?.let { parse ->
			parse.parsed.then { parse.streamOrNull?.bitByteStreamOrNull }
		}

val Stream<Bit>.bitParseByte: Parse<Bit, Byte>?
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
