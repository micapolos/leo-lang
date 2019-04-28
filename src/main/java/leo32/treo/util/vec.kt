package leo32.treo.util

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

