package leo.base

fun byte(
	bit7: Bit,
	bit6: Bit,
	bit5: Bit,
	bit4: Bit,
	bit3: Bit,
	bit2: Bit,
	bit1: Bit,
	bit0: Bit): Byte = 0
	.or(bit7.int.shl(7))
	.or(bit6.int.shl(6))
	.or(bit5.int.shl(5))
	.or(bit4.int.shl(4))
	.or(bit3.int.shl(3))
	.or(bit2.int.shl(2))
	.or(bit1.int.shl(1))
	.or(bit0.int.shl(0)).clampedByte

fun <R> Byte.foldBits(initial: R, fn: (R, Bit) -> R): R {
	val b8 = initial
	val b7 = fn(b8, toInt().and(0b10000000).clampedBit)
	val b6 = fn(b7, toInt().and(0b01000000).clampedBit)
	val b5 = fn(b6, toInt().and(0b00100000).clampedBit)
	val b4 = fn(b5, toInt().and(0b00010000).clampedBit)
	val b3 = fn(b4, toInt().and(0b00001000).clampedBit)
	val b2 = fn(b3, toInt().and(0b00000100).clampedBit)
	val b1 = fn(b2, toInt().and(0b00000010).clampedBit)
	val b0 = fn(b1, toInt().and(0b00000001).clampedBit)
	return b0
}

val Byte.int
	get() =
		toInt()

val Byte.char
	get() =
		toChar()

val Byte.bitStack: Stack<Bit>
	get() =
		foldBits(nullStack()) { bitStackOrNull, bit ->
			bitStackOrNull.push(bit)
		}!!

val Byte.bitStream: Stream<Bit>
	get() =
		bitStack.reverse.stream

val Stream<Byte>.byteBitStream: Stream<Bit>
	get() =
		first.bitStream
			.then { nextOrNull?.byteBitStream }

// TODO: This is insane, can it be made simpler? Like fold n-times?
val Stream<Bit>.bitByteStreamOrNull: Stream<Byte>?
	get() =
		first.int.let { int1 ->
			nextOrNull?.run {
				int1.shl(1).or(first.int).let { int2 ->
					nextOrNull?.run {
						int2.shl(1).or(first.int).let { int3 ->
							nextOrNull?.run {
								int3.shl(1).or(first.int).let { int4 ->
									nextOrNull?.run {
										int4.shl(1).or(first.int).let { int5 ->
											nextOrNull?.run {
												int5.shl(1).or(first.int).let { int6 ->
													nextOrNull?.run {
														int6.shl(1).or(first.int).let { int7 ->
															nextOrNull?.run {
																Stream(int7.shl(1).or(first.int).clampedByte) {
																	nextOrNull?.bitByteStreamOrNull
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}