package leo5.asm

data class JumpTable(@Suppress("ArrayInDataClass") val intArray: IntArray)

fun jumpTable(vararg ints: Int) = JumpTable(ints)
operator fun JumpTable.get(index: Int) = intArray[index]
