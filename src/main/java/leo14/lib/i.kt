package leo14.lib

import leo14.typed.Line
import leo14.typed.lineTo
import leo14.typed.type

val i2TypeLine =
	"i2" lineTo type(
		"hi" lineTo type(bitTypeLine),
		"lo" lineTo type(bitTypeLine))

val i4TypeLine =
	"i4" lineTo type(
		"hi" lineTo type(i2TypeLine),
		"lo" lineTo type(i2TypeLine))

val i8TypeLine =
	"i8" lineTo type(
		"hi" lineTo type(i4TypeLine),
		"lo" lineTo type(i4TypeLine))

val i16TypeLine =
	"i16" lineTo type(
		"hi" lineTo type(i8TypeLine),
		"lo" lineTo type(i8TypeLine))

val i32TypeLine =
	"i32" lineTo type(
		"hi" lineTo type(i16TypeLine),
		"lo" lineTo type(i16TypeLine))

val i64TypeLine =
	"i64" lineTo type(
		"hi" lineTo type(i32TypeLine),
		"lo" lineTo type(i32TypeLine))

data class I2(override val term: Term) : Obj() {
	override val typeLine: Line get() = i2TypeLine
	override fun toString() = super.toString()

	constructor(hi: Bit, lo: Bit) : this(pair(hi, lo))

	val hi get() = pairFirst(::Bit)
	val lo get() = pairSecond(::Bit)
}

data class I4(override val term: Term) : Obj() {
	override val typeLine: Line get() = i4TypeLine
	override fun toString() = super.toString()

	constructor(hi: I2, lo: I2) : this(pair(hi, lo))

	val hi get() = pairFirst(::I2)
	val lo get() = pairSecond(::I2)
}

data class I8(override val term: Term) : Obj() {
	override val typeLine: Line get() = i8TypeLine
	override fun toString() = super.toString()

	constructor(hi: I4, lo: I4) : this(pair(hi, lo))

	val hi get() = pairFirst(::I4)
	val lo get() = pairSecond(::I4)
}

data class I16(override val term: Term) : Obj() {
	override val typeLine: Line get() = i16TypeLine
	override fun toString() = super.toString()

	constructor(hi: I8, lo: I8) : this(pair(hi, lo))

	val hi get() = pairFirst(::I8)
	val lo get() = pairSecond(::I8)
}

data class I32(override val term: Term) : Obj() {
	override val typeLine: Line get() = i32TypeLine
	override fun toString() = super.toString()

	constructor(hi: I16, lo: I16) : this(pair(hi, lo))

	val hi get() = pairFirst(::I16)
	val lo get() = pairSecond(::I16)
}

data class I64(override val term: Term) : Obj() {
	override val typeLine: Line get() = i64TypeLine
	override fun toString() = super.toString()

	constructor(hi: I32, lo: I32) : this(pair(hi, lo))

	val hi get() = pairFirst(::I32)
	val lo get() = pairSecond(::I32)
}

val Int.i2 get() = I2(ushr(1).bit, bit)
val Int.i4 get() = I4(ushr(2).i2, i2)
val Int.i8 get() = I8(ushr(4).i4, i4)
val Int.i16 get() = I16(ushr(8).i8, i8)
val Int.i32 get() = I32(ushr(16).i16, i16)
val Byte.i8 get() = toInt().i8
val Short.i16 get() = toInt().i16
val Long.i64 get() = I64(ushr(32).toInt().i32, toInt().i32)

val I2.int get() = hi.int.shl(1).or(lo.int)
val I4.int get() = hi.int.shl(2).or(lo.int)
val I8.int get() = hi.int.shl(4).or(lo.int)
val I16.int get() = hi.int.shl(8).or(lo.int)
val I32.int get() = hi.int.shl(16).or(lo.int)
val I64.long get() = hi.int.toLong().shl(32).or(lo.int.toLong())
val I8.byte get() = int.toByte()
val I16.short get() = int.toShort()
