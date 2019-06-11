package leo5.asm

data class Table(@Suppress("ArrayInDataClass") val intArray: IntArray)
fun table(vararg ints: Int) = Table(ints)
operator fun Table.get(index: Int) = intArray[index]
