package leo5.asm

data class Offset(val int: Int)

fun offset(int: Int) = Offset(int)
fun intOffset(int: Int) = offset(int shl 2)
