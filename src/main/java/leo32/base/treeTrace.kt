package leo32.base

import leo.binary.Bit
import leo.binary.nextOrNull

data class TreeTrace<out T>(
	val cursor: TreeCursor<T>,
	val bit: Bit)

infix fun <T> TreeCursor<T>.traceTo(bit: Bit) =
	TreeTrace(this, bit)

val <T> TreeTrace<T>.nextCursorOrNull: TreeCursor<T>?
	get() =
		bit.nextOrNull
			?.let { nextBit -> cursor to nextBit }
			?: cursor.traceOrNull?.nextCursorOrNull