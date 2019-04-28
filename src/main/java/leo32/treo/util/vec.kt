package leo32.treo.util

import leo.binary.Bit
import leo.binary.bit0
import leo.binary.int
import leo32.treo.Var

data class Vec2<T>(val hi: T, val lo: T)
data class Vec4<T>(val hi: Vec2<T>, val lo: Vec2<T>)
data class Vec8<T>(val hi: Vec4<T>, val lo: Vec4<T>)
data class Vec16<T>(val hi: Vec8<T>, val lo: Vec8<T>)
data class Vec32<T>(val hi: Vec16<T>, val lo: Vec16<T>)
data class Vec64<T>(val hi: Vec32<T>, val lo: Vec32<T>)

inline fun <T> vec2(fn: () -> T) = Vec2(fn(), fn())
inline fun <T> vec4(fn: () -> T) = Vec4(vec2(fn), vec2(fn))
inline fun <T> vec8(fn: () -> T) = Vec8(vec4(fn), vec4(fn))
inline fun <T> vec16(fn: () -> T) = Vec16(vec8(fn), vec8(fn))
inline fun <T> vec32(fn: () -> T) = Vec32(vec16(fn), vec16(fn))
inline fun <T> vec64(fn: () -> T) = Vec64(vec32(fn), vec32(fn))

val Vec2<Bit>.bitInt get() = hi.int.shl(1).or(lo.int)
val Vec4<Bit>.bitInt get() = hi.bitInt.shl(2).or(lo.bitInt)
val Vec8<Bit>.bitInt get() = hi.bitInt.shl(4).or(lo.bitInt)
val Vec16<Bit>.bitInt get() = hi.bitInt.shl(8).or(lo.bitInt)
val Vec32<Bit>.bitInt get() = hi.bitInt.shl(16).or(lo.bitInt)
val Vec64<Bit>.bitLong get() = hi.bitInt.toLong().shl(32).or(lo.bitInt.toLong().and(0xFFFFFFFF))
val Vec32<Bit>.bitFloat get() = Float.fromBits(bitInt)
val Vec64<Bit>.bitDouble get() = Double.fromBits(bitLong)

var Vec2<Var>.varInt
	get() = hi.bit.int.shl(1).or(lo.bit.int)
	set(int) {
		hi.bit = int.shr(1).bit0; lo.bit = int.bit0
	}

var Vec4<Var>.varInt
	get() = hi.varInt.shl(2).or(lo.varInt)
	set(int) {
		hi.varInt = int.shr(2); lo.varInt = int
	}

var Vec8<Var>.varInt
	get() = hi.varInt.shl(4).or(lo.varInt)
	set(int) {
		hi.varInt = int.shr(4); lo.varInt = int
	}

var Vec16<Var>.varInt
	get() = hi.varInt.shl(8).or(lo.varInt)
	set(int) {
		hi.varInt = int.shr(8); lo.varInt = int
	}

var Vec32<Var>.varInt
	get() = hi.varInt.shl(16).or(lo.varInt)
	set(int) {
		hi.varInt = int.shr(16); lo.varInt = int
	}

var Vec64<Var>.varLong: Long
	get() = hi.varInt.toLong().shl(32).or(lo.varInt.toLong().and(0xFFFFFFFF))
	set(long) {
		hi.varInt = long.ushr(32).toInt(); lo.varInt = long.toInt()
	}

var Vec32<Var>.varFloat
	get() = Float.fromBits(varInt)
	set(float) {
		varInt = float.toRawBits()
	}

var Vec64<Var>.varDouble
	get() = Double.fromBits(varLong)
	set(double) {
		varLong = double.toRawBits()
	}
