package leo32.c

data class mask(val int: Int)

fun Int.invoke(mask: mask) = and(mask.int)
