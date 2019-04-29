package leo32.c

data class shift(val int: Int)

fun Int.right(shift: shift) = ushr(shift.int)
fun Int.left(shift: shift) = shl(shift.int)
