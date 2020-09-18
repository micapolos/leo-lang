package vm2

import java.lang.Integer.numberOfLeadingZeros
import java.lang.Integer.numberOfTrailingZeros

sealed class Numeric
data class IntegerValue(val integer: Integer) : Numeric()
data class FloatingPointValue(val floatingPoint: FloatingPoint) : Numeric()

sealed class Integer : Numeric()
data class I32(val int: Int) : Integer()
data class I64(val long: Long) : Integer()

sealed class FloatingPoint : Numeric()
data class F32(val float: Float) : FloatingPoint()
data class F64(val double: Double) : FloatingPoint()

val Int.i32 get() = I32(this)
val Long.i64 get() = I64(this)
val Float.f32 get() = F32(this)
val Double.f64 get() = F64(this)
val Integer.numeric get() = IntegerValue(this)
val FloatingPoint.numeric get() = FloatingPointValue(this)

operator fun I32.plus(i32: I32) = I32(int + i32.int)
operator fun I32.minus(i32: I32) = I32(int - i32.int)
operator fun I32.times(i32: I32) = I32(int * i32.int)
operator fun I32.div(i32: I32) = I32(int / i32.int)
operator fun I32.rem(i32: I32) = I32(int % i32.int)

val I32.leadingZerosCount get() = numberOfLeadingZeros(int).i32
val I32.trailingZerosCount get() = numberOfTrailingZeros(int).i32
val I32.onesCount get() = java.lang.Integer.bitCount(int).i32

operator fun I64.plus(i64: I64) = I64(long + i64.long)
operator fun I64.minus(i64: I64) = I64(long - i64.long)
operator fun I64.times(i64: I64) = I64(long * i64.long)
operator fun I64.div(i64: I64) = I64(long / i64.long)
operator fun I64.rem(i64: I64) = I64(long % i64.long)

val I64.leadingZerosCount: I64 get() = TODO()
val I64.trailingZerosCount: I64 get() = TODO()
val I64.onesCount: I64 get() = TODO()
