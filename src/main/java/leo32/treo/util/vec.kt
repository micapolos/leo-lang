package leo32.treo.util

import leo.binary.clampedBit
import leo.binary.int
import leo32.treo.Variable

data class Vec2<T>(val hi: T, val lo: T)
data class Vec4<T>(val hi: Vec2<T>, val lo: Vec2<T>)
data class Vec8<T>(val hi: Vec4<T>, val lo: Vec4<T>)
data class Vec16<T>(val hi: Vec8<T>, val lo: Vec8<T>)
data class Vec32<T>(val hi: Vec16<T>, val lo: Vec16<T>)
data class Vec64<T>(val hi: Vec32<T>, val lo: Vec32<T>)

fun <T> vec2(fn: () -> T) = Vec2(fn(), fn())
fun <T> vec4(fn: () -> T) = Vec4(vec2(fn), vec2(fn))
fun <T> vec8(fn: () -> T) = Vec8(vec4(fn), vec4(fn))
fun <T> vec16(fn: () -> T) = Vec16(vec8(fn), vec8(fn))
fun <T> vec32(fn: () -> T) = Vec32(vec16(fn), vec16(fn))
fun <T> vec64(fn: () -> T) = Vec64(vec32(fn), vec32(fn))

var Vec2<Variable>.int
	get() = hi.bit.int.shl(1).or(lo.bit.int)
	set(int) {
		hi.bit = int.ushr(1).clampedBit; lo.bit = int.clampedBit
	}

var Vec4<Variable>.int
	get() = hi.int.shl(2).or(lo.int)
	set(int) {
		hi.int = int.ushr(2); lo.int = int
	}

var Vec8<Variable>.int
	get() = hi.int.shl(4).or(lo.int)
	set(int) {
		hi.int = int.ushr(4); lo.int = int
	}

var Vec16<Variable>.int
	get() = hi.int.shl(8).or(lo.int)
	set(int) {
		hi.int = int.ushr(8); lo.int = int
	}

var Vec32<Variable>.int
	get() = hi.int.shl(16).or(lo.int)
	set(int) {
		hi.int = int.ushr(16); lo.int = int
	}

var Vec64<Variable>.long
	get() = hi.int.toLong().shl(32).or(lo.int.toLong())
	set(long) {
		hi.int = long.ushr(32).toInt(); lo.int = long.toInt()
	}

var Vec32<Variable>.float
	get() = Float.fromBits(int)
	set(float) {
		int = float.toRawBits()
	}

var Vec64<Variable>.double
	get() = Double.fromBits(long)
	set(double) {
		long = double.toRawBits()
	}
