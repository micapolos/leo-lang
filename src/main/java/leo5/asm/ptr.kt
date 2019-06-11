package leo5.asm

data class Ptr(val int: Int)

fun ptr(int: Int) = Ptr(int)
fun intPtr(int: Int) = ptr(int shl 2)
