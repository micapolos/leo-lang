package leo5.asm

data class ArrayInputState(@Suppress("ArrayInDataClass") val array: IntArray, var index: Int)

fun input(vararg ints: Int) = ArrayInputState(ints, 0).run { input { array[index++] } }
