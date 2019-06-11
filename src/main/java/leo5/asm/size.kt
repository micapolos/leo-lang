package leo5.asm

data class Size(val int: Int)

fun size(int: Int) = Size(int)
fun intSize(int: Int) = size(int shl 2)
