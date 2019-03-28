package leo.base

import leo.binary.Zero

val Char.clampedByte
	get() =
		toByte()

val Char.int
	get() =
		toInt()

@Suppress("unused")
val Zero.char
	get() =
		'\u0000'