package leo32.base

import leo.binary.Bit

data class TreeTrace<out T>(
	val cursor: TreeCursor<T>,
	val bit: Bit)

infix fun <T> TreeCursor<T>.traceTo(bit: Bit) =
	TreeTrace(this, bit)
