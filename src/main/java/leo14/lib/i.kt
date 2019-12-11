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

class I2(term: Term) : Obj(term) {
	override val typeLine: Line get() = i2TypeLine

	constructor(hi: Bit, lo: Bit) : this(struct2(hi, lo))

	val hi get() = struct2get1(::Bit)
	val lo get() = struct2get2(::Bit)
}

class I4(term: Term) : Obj(term) {
	override val typeLine: Line get() = i4TypeLine

	constructor(hi: I2, lo: I2) : this(struct2(hi, lo))

	val hi get() = struct2get1(::I2)
	val lo get() = struct2get2(::I2)
}

class I8(term: Term) : Obj(term) {
	override val typeLine: Line get() = i8TypeLine

	constructor(hi: I4, lo: I4) : this(struct2(hi, lo))

	val hi get() = struct2get1(::I4)
	val lo get() = struct2get2(::I4)
}

class I16(term: Term) : Obj(term) {
	override val typeLine: Line get() = i16TypeLine

	constructor(hi: I8, lo: I8) : this(struct2(hi, lo))

	val hi get() = struct2get1(::I8)
	val lo get() = struct2get2(::I8)
}

class I32(term: Term) : Obj(term) {
	override val typeLine: Line get() = i32TypeLine

	constructor(hi: I16, lo: I16) : this(struct2(hi, lo))

	val hi get() = struct2get1(::I16)
	val lo get() = struct2get2(::I16)
}

class I64(term: Term) : Obj(term) {
	override val typeLine: Line get() = i64TypeLine

	constructor(hi: I32, lo: I32) : this(struct2(hi, lo))

	val hi get() = struct2get1(::I32)
	val lo get() = struct2get2(::I32)
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
