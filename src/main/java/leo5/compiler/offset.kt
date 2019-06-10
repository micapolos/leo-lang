package leo5.compiler

data class Offset(val int: Int)

fun offset(int: Int) = Offset(int)
operator fun Offset.plus(size: Size) = offset(int + size.int)
