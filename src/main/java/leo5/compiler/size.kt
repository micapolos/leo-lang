package leo5.compiler

data class Size(val int: Int)

fun size(int: Int) = Size(int)
operator fun Size.plus(size: Size) = size(int + size.int)
val Size.offset get() = offset(int)